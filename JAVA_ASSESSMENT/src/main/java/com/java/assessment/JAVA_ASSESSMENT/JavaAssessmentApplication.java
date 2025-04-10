package com.java.assessment.JAVA_ASSESSMENT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JavaAssessmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaAssessmentApplication.class, args);
	}

}
