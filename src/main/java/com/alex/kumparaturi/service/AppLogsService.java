package com.alex.kumparaturi.service;

import com.alex.kumparaturi.model.AppLogs;
import com.alex.kumparaturi.repository.AppLogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppLogsService {
    @Autowired
    AppLogsRepository appLogsRepository;

    public void saveLogToDatabase(String message){
        AppLogs appLogs = new AppLogs();
        appLogs.setMessage(message);
        appLogsRepository.save(appLogs);
    }
}
