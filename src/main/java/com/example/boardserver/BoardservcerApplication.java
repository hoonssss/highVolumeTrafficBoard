package com.example.boardserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BoardservcerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardservcerApplication.class, args);
	}

}
