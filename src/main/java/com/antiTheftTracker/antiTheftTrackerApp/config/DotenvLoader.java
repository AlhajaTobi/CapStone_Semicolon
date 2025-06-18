package com.antiTheftTracker.antiTheftTrackerApp.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvLoader {

    @PostConstruct
    public void init(){
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach((entry) ->
                System.setProperty(entry.getKey(), entry.getValue()));
    }
}
