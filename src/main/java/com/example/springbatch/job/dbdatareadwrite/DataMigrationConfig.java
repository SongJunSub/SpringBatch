package com.example.springbatch.job.dbdatareadwrite;

import com.example.springbatch.domain.Accounts;
import com.example.springbatch.domain.Orders;
import com.example.springbatch.repository.AccountsRepository;
import com.example.springbatch.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
 * Desc : 주문 테이블 -> 정산 테이블 데이터 이관
 * Run : --spring.batch.job.name=dbMigrationJob
 */
@Configuration
@RequiredArgsConstructor
public class DataMigrationConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final OrderRepository orderRepository;
    private final AccountsRepository accountsRepository;

    @Bean
    public Job dbMigrationJob(Step dbMigrationStep) {
        return new JobBuilder("dbMigrationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(dbMigrationStep)
                .build();
    }

    @Bean
    @JobScope
    public Step dbMigrationStep(ItemReader<Orders> ordersReader, ItemProcessor<Orders, Accounts> migrationProcessor, ItemWriter<Accounts> accountsWriter) {
        SimpleCompletionPolicy simpleCompletionPolicy = new SimpleCompletionPolicy(5);

        return new StepBuilder("dbMigrationStep", jobRepository)
                .<Orders, Accounts>chunk(simpleCompletionPolicy, transactionManager) // 몇 개씩 데이터 처리를 할건지 사이즈를 명시해줘야 한다.
                .reader(ordersReader)
                //.writer(chunk -> chunk.forEach(System.out::println))
                /*.writer(new ItemWriter<Orders>() {
                    @Override
                    public void write(Chunk<? extends Orders> chunk) throws Exception {
                        chunk.forEach(System.out::println);
                    }
                })*/
                .processor(migrationProcessor)
                .writer(accountsWriter)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemWriter<Accounts> accountsWriter() {
        return new RepositoryItemWriterBuilder<Accounts>()
                .repository(accountsRepository)
                .methodName("save")
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<Accounts> accountsWriterSample() {
        return new ItemWriter<Accounts>() {
            @Override
            public void write(Chunk<? extends Accounts> chunk) throws Exception {
                chunk.forEach(item -> accountsRepository.save(item));
            }
        };
    }

    @Bean
    @StepScope
    public ItemProcessor<Orders, Accounts> migrationProcessor() {
        return new ItemProcessor<Orders, Accounts>() {
            @Override
            public Accounts process(Orders item) throws Exception {
                return new Accounts(item);
            }
        };
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Orders> ordersReader() {
        return new RepositoryItemReaderBuilder<Orders>()
                .name("orderReader")
                .repository(orderRepository)
                .methodName("findAll")
                .pageSize(5)
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

}