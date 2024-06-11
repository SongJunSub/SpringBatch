package com.example.springbatch.job.filedatareadwrite;

import com.example.springbatch.dto.PlayerDTO;
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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

/*
 * Desc : 파일 읽고 쓰기
 * Run : --spring.batch.job.name=fileReadAndWriteJob
 */
@Configuration
@RequiredArgsConstructor
public class FileDataReadWriteConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job fileReadAndWriteJob(Step fileReadAndWriteStep) {
        return new JobBuilder("fileReadAndWriteJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(fileReadAndWriteStep)
                .build();
    }

    @Bean
    @JobScope
    public Step fileReadAndWriteStep(ItemReader<PlayerDTO> playerDTOItemReader) {
        SimpleCompletionPolicy simpleCompletionPolicy = new SimpleCompletionPolicy(5);

        return new StepBuilder("fileReadAndWriteStep", jobRepository)
                .<PlayerDTO, PlayerYearsDTO>chunk(simpleCompletionPolicy, transactionManager)
                .reader(playerDTOItemReader)
                /*.writer(new ItemWriter<PlayerDTO>() {
                    @Override
                    public void write(Chunk<? extends PlayerDTO> chunk) throws Exception {
                        chunk.forEach(System.out::println);
                    }
                })*/
                .processor()
                .writer()
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<PlayerDTO> playerDTOItemReader() {
        return new FlatFileItemReaderBuilder<PlayerDTO>()
                .name("playerDTOItemReader")
                .resource(new FileSystemResource("Players.csv"))
                .lineTokenizer(new DelimitedLineTokenizer() {{
                    setNames("id", "lastName", "firstName", "position", "birthYear", "debutYear");
                }})
                .fieldSetMapper(new PlayerFieldSetMapper())
                .linesToSkip(1) // 첫 번째 라인은 제목이므로 스킵한다.
                .build();
    }

}