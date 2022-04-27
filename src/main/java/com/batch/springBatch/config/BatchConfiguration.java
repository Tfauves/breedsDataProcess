package com.batch.springBatch.config;


import com.batch.springBatch.domain.BtcData;
import com.batch.springBatch.processor.BtcDataProcessor;
import com.batch.springBatch.repositories.BtcDataRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration // Informs Spring that this class contains configurations
@EnableBatchProcessing // Enables batch processing for the application
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Lazy
    public BtcDataRepository btcDataRepository;

    @Autowired
    DataSource dataSource;

    // Reads the data.csv file and creates instances of the btcData entity for each from the .csv file.
    @Bean
    public FlatFileItemReader<BtcData> reader() {
        return new FlatFileItemReaderBuilder<BtcData>()
                .name("btcDataFileReader")
                .resource(new ClassPathResource("/data/testBtcData.csv"))
                .delimited()
                .names(new String[]{"unix_timestamp", "datetime", "open", "high", "low", "close", "volume_btc", "volume_currency", "weighted_price"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(BtcData.class);
                }})
                .build();
    }


    // Creates the Writer, configuring the repository and the method that will be used to save the data into the database
    @Bean
    public RepositoryItemWriter<BtcData> writer() {
        RepositoryItemWriter<BtcData> iwriter = new RepositoryItemWriter<>();
        JdbcBatchItemWriter<BtcData> itemWriter = new JdbcBatchItemWriter<>();
        iwriter.setRepository(btcDataRepository);
        itemWriter.setSql("INSERT INTO btc_data  VALUES (:id, :unix_timestamp, :datetime, :open, :high, :low, :close, :volume_btc, :volume_currency, :weighted_price)");

        iwriter.setMethodName("save");
        return iwriter;
    }


    // Creates an instance of BtcDataProcessor that converts one data form to another. In our case the data form is maintained.
    @Bean
    public BtcDataProcessor processor() {
        return new BtcDataProcessor();
    }

    // Batch jobs are built from steps. A step contains the reader, processor and the writer.
    @Bean
    public Step step1(ItemReader<BtcData> itemReader, ItemWriter<BtcData> itemWriter)
            throws Exception {

        return this.stepBuilderFactory.get("step1")
                .<BtcData, BtcData>chunk(5)
                .reader(itemReader)
                .processor(processor())
                .writer(itemWriter)
                .build();
    }

    // Executes the job, saving the data from .csv file into the database.
    @Bean
    public Job btcDataJob(JobCompletionListener listener, Step step1)
            throws Exception {

        return this.jobBuilderFactory.get("btc data job").incrementer(new RunIdIncrementer())
                .listener(listener).start(step1).build();
    }
}
