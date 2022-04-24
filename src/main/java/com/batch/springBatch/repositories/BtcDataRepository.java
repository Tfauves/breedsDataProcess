package com.batch.springBatch.repositories;

import com.batch.springBatch.domain.BtcData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BtcDataRepository extends JpaRepository<BtcData, Long> {
}
