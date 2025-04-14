package com.example.data_ingestion_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private IngestionFileReader ingestionFileReader;

    @Scheduled(initialDelay = 1000)
    public void ingestFiles() {
        ingestionFileReader.readFiles();
    }
}
