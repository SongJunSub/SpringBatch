package com.example.springbatch.job.joblistener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
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
 * Run : --spring.batch.job.name=jobListenerConfig
 */
@Configuration
@RequiredArgsConstructor
public class JobListenerConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job jobListenerJob(Step jobListenerStep) {
        return new JobBuilder("jobListenerJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(new JobLoggerListener())
                .start(jobListenerStep)
                .build();
    }

    @Bean
    public Step jobListenerStep(Tasklet jobListenerTasklet) {
        return new StepBuilder("jobListenerStep", jobRepository)
                .tasklet(jobListenerTasklet, transactionManager)
                .build();
    }

    @Bean
    public Tasklet jobListenerTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("Job Listener Test");

            return RepeatStatus.FINISHED;
        };
    }

}