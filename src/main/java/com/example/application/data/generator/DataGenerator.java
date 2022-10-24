package com.example.application.data.generator;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.application.data.entity.Contas;
import com.example.application.data.entity.Status;
import com.example.application.data.repository.ContasRepository;
import com.example.application.data.repository.StatusRepository;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;


@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(ContasRepository contasRepository,
                                      StatusRepository statusRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (contasRepository.count() != 0L) {
                logger.info("Usando um Banco de Dados Existente!");
                return;
            }
            int seed = 123;

            logger.info("Gerando informações demonstrativas...");

            List<Status> statuses = statusRepository
                    .saveAll(Stream.of("Corrente", "Poupança")
                            .map(Status::new).collect(Collectors.toList()));

            logger.info("... gerando 5 Contas aleatorias...");
            ExampleDataGenerator<Contas> contasGenerator = new ExampleDataGenerator<>(Contas.class,
                    LocalDateTime.now());
            contasGenerator.setData(Contas::setConta, DataType.COMPANY_NAME);
            contasGenerator.setData(Contas::setSaldo, DataType.PRICE);

            Random r = new Random(seed);
            List<Contas> conta = contasGenerator.create(5, seed).stream().peek(contact -> {
                contact.setStatus(statuses.get(r.nextInt(statuses.size())));
            }).collect(Collectors.toList());

            contasRepository.saveAll(conta);

            logger.info("Contas geradas");
        };
    }

}
