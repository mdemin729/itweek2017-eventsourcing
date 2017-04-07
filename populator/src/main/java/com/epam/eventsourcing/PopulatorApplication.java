package com.epam.eventsourcing;

import com.epam.eventsourcing.event.PersonCreated;
import com.epam.eventsourcing.event.PersonEmailChanged;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Slf4j
@SpringBootApplication
public class PopulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PopulatorApplication.class, args);
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Component
    public static class PopulatorCommand implements ApplicationRunner {

        @Autowired
        private EventPublisher publisher;

        @Override
        public void run(ApplicationArguments args) throws Exception {

            int count = 0;

            for (int i = 0; i < 10000; i++) {
                UUID id = UUID.randomUUID();
                publisher.publish(PersonCreated.builder()
                        .id(id)
                        .name("test" + 1)
                        .email("test" + i + "@test.com")
                        .gender(i % 2 == 0 ? Gender.FEMALE : Gender.MALE)
                        .build()
                );
                count++;
                for (int j = 0; j < new Random().nextInt(10); j++) {
                    publisher.publish(PersonEmailChanged.builder()
                            .id(id)
                            .email("test" + i + "_" + j + "@test.com")
                            .build()
                    );
                    count++;
                }
            }

            log.info(count + " events published");
        }
    }
}
