package com.example.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
@RequiredArgsConstructor
public class SpringBatchApplication {

    private final JobLauncher jobLauncher;

    private final Job testJob;
    private final Job validatedParamJob;

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            jobLauncher.run(validatedParamJob, new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                            .addString("fileName", "test.csv")
                    .toJobParameters());
        };
    }

}