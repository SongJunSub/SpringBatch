package com.example.springbatch.job.conditionalstep;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/*
 * Desc : Step 결과에 따른 다음 Step 분기 처리
 * Run : --spring.batch.job.name=conditionalStepJob
 */
@Configuration
@RequiredArgsConstructor
public class ConditionalStepJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job conditionalStepJob(Step conditionalStartStep,
                                  Step conditionalAllStep,
                                  Step conditionalFailedStep,
                                  Step conditionalCompletedStep) {
        return new JobBuilder("conditionalStepJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(conditionalStartStep)
                    .on("FAILED").to(conditionalFailedStep) // conditionalStartStep이 실패하면 conditionalFailedStep 스텝이 실행된다.
                .from(conditionalStartStep)
                    .on("COMPLETED").to(conditionalCompletedStep) // conditionalStartStep이 성공하면 conditionalCompletedStep 스텝이 실행된다.
                .from(conditionalStartStep)
                    .on("*").to(conditionalAllStep) // FAILED, COMPLETED 이외의 상태 결과를 받았다면 conditionalAllStep 스텝이 실행된다.
                .end()
                .build();
    }

    @Bean
    @JobScope
    public Step conditionalStartStep() {
        return new StepBuilder("conditionalStartStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("Conditional Start Step");

                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step conditionalAllStep() {
        return new StepBuilder("conditionalAllStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("Conditional All Step");

                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step conditionalFailedStep() {
        return new StepBuilder("conditionalFailedStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("Conditional Failed Step");

                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step conditionalCompletedStep() {
        return new StepBuilder("conditionalCompletedStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    System.out.println("Conditional Completed Step");

                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

}