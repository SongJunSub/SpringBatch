package com.example.springbatch.job.dbdatareadwrite;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/*
 * Desc : 주문 테이블 -> 정산 테이블 데이터 이관
 * Run : --spring.batch.job.name=dbMigrationJob
 */
@Configuration
@RequiredArgsConstructor
public class DataMigrationConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job dbMigrationJob(Step dbMigrationStep) {
        return new JobBuilder("dbMigrationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(dbMigrationStep)
                .build();
    }

    @Bean
    public Step dbMigrationStep() {
        return new StepBuilder("dbMigrationStep", jobRepository)
                .tasklet(dbMigrationTasklet(), transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet dbMigrationTasklet() {

    }

}