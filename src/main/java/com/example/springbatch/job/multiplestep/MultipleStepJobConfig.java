package com.example.springbatch.job.multiplestep;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class MultipleStepJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job multipleStepJob(Step multipleStep1, Step multipleStep2, Step multipleStep3) {
        return new JobBuilder("multipleStepJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(multipleStep1)
                .next(multipleStep2)
                .next(multipleStep3)
                .build();
    }

    @Bean
    @JobScope
    public Step multipleStep1() {
        return new StepBuilder("multipleStep1", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("Step 1");

                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step multipleStep2() {
        return new StepBuilder("multipleStep2", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("Step 2");

                    // Step 2의 정보를 ExecutionContext를 이용하여 데이터를 담은 후 Step 3에서 호출하여 사용할 수 있다.
                    ExecutionContext executionContext = chunkContext
                            .getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();

                    executionContext.put("someKey", "Step 2 Key");

                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step multipleStep3() {
        return new StepBuilder("multipleStep3", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("Step 3");

                    ExecutionContext executionContext = chunkContext
                            .getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();

                    System.out.println(executionContext.get("someKey"));

                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

}