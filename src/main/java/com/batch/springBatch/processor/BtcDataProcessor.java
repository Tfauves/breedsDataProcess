package com.batch.springBatch.processor;

import com.batch.springBatch.domain.BtcData;
import com.batch.springBatch.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class BtcDataProcessor implements ItemProcessor<BtcData, BtcData> {


    private static final Logger logger = LoggerFactory.getLogger(BtcDataProcessor.class);

    @Override
    public BtcData process(final BtcData item) throws Exception {
        final Long id = item.getId();
        final String unix_timestamp = item.getUnix_timestamp();
        final String datetime = item.getDatetime();
        final String open = item.getOpen();
        final String high = item.getHigh();
        final String low = item.getLow();
        final String close = item.getClose();
        final String volume_btc = item.getVolume_btc();
        final String volume_currency = item.getVolume_currency();
        final String weighted_price = item.getWeighted_price();
        // Creates a new instance of Person
        final BtcData transformedData = new BtcData(1L, unix_timestamp, datetime, open, high, low, close, volume_btc, volume_currency, weighted_price);
        // logs the person entity to the application logs
        logger.info("Converting (" + item + ") into (" + transformedData + ")");
        return transformedData;
    }
}
