package com.example.springbatch.job;

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

@Configuration
@RequiredArgsConstructor
public class TestJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job testJob() {
        return new JobBuilder("testJob", jobRepository) // Job Name 부여
                .incrementer(new RunIdIncrementer()) // Sequence 부여
                .start(testStep()) // Step Start
                .build();
    }

    @Bean
    public Step testStep() {
        return new StepBuilder("testStep", jobRepository)
                .tasklet(testTasklet(), transactionManager) // Step 하위에는 ItemReader, ItemProcessor, ItemWriter가 존재하는데, 읽고 쓰는 것 없이 단순한 배치를 만들고 싶을 때는 tasklet 방식을 활용한다.
                .build();
    }

    @Bean
    public Tasklet testTasklet() {
        // 아래 코드를 람다 표현식을 적용한 코드로 변환하면
        return (contribution, chunkContext) -> {
            System.out.println("Spring Batch Test");

            return RepeatStatus.FINISHED;
        };

        /*return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("Spring Batch Test");

                return RepeatStatus.FINISHED;
            }
        };*/
    }

}