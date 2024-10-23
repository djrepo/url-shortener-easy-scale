package com.upwork.tinyurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
@EnableCassandraRepositories("com.upwork.tinyurl.repository")
public class TinyUrlApplication {

        public static void main(String[] args) {
                SpringApplication.run(TinyUrlApplication.class, args);
        }
}