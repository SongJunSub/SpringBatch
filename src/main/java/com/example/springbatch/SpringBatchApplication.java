package com.example.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
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
    private final Job jobListenerJob;
    private final Job dbMigrationJob;
    private final Job fileReadAndWriteJob;
    private final Job multipleStepJob;
    private final Job conditionalStepJob;

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            try {
                jobLauncher.run(conditionalStepJob, new JobParametersBuilder()
                        .addLong("startAt", System.currentTimeMillis())
                        .toJobParameters());
            }
            catch (Exception e) {
                System.out.println("Error Message: " + e.getMessage());
            }
        };
    }

}