package com.example.springbatch.job.dbdatareadwrite;

import com.example.springbatch.SpringBatchTestConfig;
import com.example.springbatch.domain.Orders;
import com.example.springbatch.repository.AccountsRepository;
import com.example.springbatch.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
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

import java.util.Date;

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

    @AfterEach
    public void cleanUpEach() {
        orderRepository.deleteAll();
        accountsRepository.deleteAll();
    }

    @Test
    public void successWithNoData() throws Exception {
        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // Then
        Assertions.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(0, accountsRepository.count());
    }

    @Test
    public void successWithData() throws Exception {
        // Given
        Orders orders1 = new Orders(null, "Kakao Gift", 15000, new Date());
        Orders orders2 = new Orders(null, "Naver Gift", 20000, new Date());

        orderRepository.save(orders1);
        orderRepository.save(orders2);

        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // Then
        Assertions.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(2, accountsRepository.count());
    }

}