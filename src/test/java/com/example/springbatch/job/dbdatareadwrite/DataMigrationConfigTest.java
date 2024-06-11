package com.example.springbatch.job.dbdatareadwrite;

import com.example.springbatch.SpringBatchTestConfig;
import com.example.springbatch.repository.AccountsRepository;
import com.example.springbatch.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = {SpringBatchTestConfig.class, DataMigrationConfig.class})
class DataMigrationConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Test()
    public void successWithNoData() throws Exception {
        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // Then
        Assertions.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(0, accountsRepository.count());
    }

}