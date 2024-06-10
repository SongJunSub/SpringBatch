package com.example.springbatch.job.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/*
 * Run : --spring.batch.job.name=validatedParamJob -fineName=test.csv
 */
@Configuration
@RequiredArgsConstructor
public class ValidatedParamJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job validatedParamJob(Step validatedParamStep) {
        return new JobBuilder("validatedParamJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(validatedParamStep)
                .build();
    }

    @Bean
    public Step validatedParamStep(Tasklet validatedParamTasklet) {
        return new StepBuilder("validatedParamStep", jobRepository)
                .tasklet(validatedParamTasklet, transactionManager)
                .build();
    }

    @Bean
    public Tasklet validatedParamTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("Validated Param Test");

            return RepeatStatus.FINISHED;
        };
    }

}