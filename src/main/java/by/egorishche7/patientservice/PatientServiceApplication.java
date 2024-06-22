package by.egorishche7.patientservice;

import by.egorishche7.patientservice.model.EGender;
import by.egorishche7.patientservice.payload.request.CreatePatientRequest;
import by.egorishche7.patientservice.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootApplication
public class PatientServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate, AuthService authService) {
        return _ -> {
            String token = authService.getToken();
            if (token != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(token);

                IntStream.range(0, 100).forEach(i -> {
                    CreatePatientRequest request = new CreatePatientRequest();
                    request.setName("Patient " + i);
                    request.setGender(new Random().nextBoolean() ? EGender.male : EGender.female);
                    request.setBirthDate(LocalDateTime.now().minusYears(20 + new Random().nextInt(30)));

                    HttpEntity<CreatePatientRequest> entity = new HttpEntity<>(request, headers);

                    try {
                        restTemplate.postForEntity("http://localhost:8081/patients", entity, String.class);
                        System.out.println("Patient " + (i + 1) + " created successfully.");
                    } catch (Exception e) {
                        System.err.println("Error creating patient " + (i + 1) + ": " + e.getMessage());
                    }
                });
            } else {
                System.err.println("Failed to obtain access token.");
            }
        };
    }
}
