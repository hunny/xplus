package com.xplus.commons.batch.spring.batchintro;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBatchIntroApplication {

	public static void main(String[] args) {

		if (args.length != 2) {
			throw new IllegalArgumentException();
		}
		String studentCsv = args[0];// src/main/resources/batch/batchintro/students.csv
		String outputCsv = args[1];// output.csv
		System.setProperty("input", "file://" + new File(studentCsv).getAbsolutePath());
		System.setProperty("output", "file://" + new File(outputCsv).getAbsolutePath());

		System.exit(SpringApplication.exit(SpringApplication.run(SpringBatchIntroApplication.class, args)));
	}
}
