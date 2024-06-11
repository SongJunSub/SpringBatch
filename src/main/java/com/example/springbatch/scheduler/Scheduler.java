package com.example.springbatch.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final JobLauncher jobLauncher;

    private final Job testJob;

    @Scheduled(cron = "0 */1 * * * *")
    public void testJobRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParametersBuilder = new JobParametersBuilder()
                .addLong("requestTime", System.currentTimeMillis())
                .toJobParameters();

        try {
            jobLauncher.run(testJob, jobParametersBuilder);
        }
        catch (JobExecutionAlreadyRunningException e) {
            System.out.println("Error Message : " + e.getMessage());
        }
    }

}