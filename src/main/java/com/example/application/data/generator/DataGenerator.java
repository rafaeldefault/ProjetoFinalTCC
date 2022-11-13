package com.example.application.data.generator;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.application.data.entity.Contas;
import com.example.application.data.entity.ContasPagar;
import com.example.application.data.entity.Despesas;
import com.example.application.data.entity.Receitas;
import com.example.application.data.entity.Status;
import com.example.application.data.repository.ContasPagarRepository;
import com.example.application.data.repository.ContasRepository;
import com.example.application.data.repository.DespesasRepository;
import com.example.application.data.repository.ReceitasRepository;
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
    		ReceitasRepository receitasRepository,
    		DespesasRepository despesasRepository,
    		ContasPagarRepository contasPagarRepository,
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
            
// ----------------------------------------------------------------------------------------------------
            
            List<Status> statuses2 = statusRepository
                    .saveAll(Stream.of("Quitada", "A Pagar")
                            .map(Status::new).collect(Collectors.toList()));

            logger.info("... gerando 5 Contas a Pagar aleatorias...");
            ExampleDataGenerator<ContasPagar> contasPagarGenerator = new ExampleDataGenerator<>(ContasPagar.class,
                    LocalDateTime.now());
            contasPagarGenerator.setData(ContasPagar::setEntidade, DataType.COMPANY_NAME);
            contasPagarGenerator.setData(ContasPagar::setValor, DataType.PRICE);
            contasPagarGenerator.setData(ContasPagar::setVencimento, DataType.DATE_NEXT_30_DAYS);


            Random r2 = new Random(seed);
            List<ContasPagar> contaPagar = contasPagarGenerator.create(5, seed).stream().peek(contact -> {
                contact.setStatus(statuses2.get(r2.nextInt(statuses2.size())));
            }).collect(Collectors.toList());

            contasPagarRepository.saveAll(contaPagar);

            logger.info("Contas geradas");
            
//--------------------------------------------------------------------------------------------------------
            

            logger.info("... gerando 5 Contas a Pagar aleatorias...");
            ExampleDataGenerator<Despesas> despesasGenerator = new ExampleDataGenerator<>(Despesas.class,
                    LocalDateTime.now());
            despesasGenerator.setData(Despesas::setNatureza, DataType.COMPANY_NAME);
            despesasGenerator.setData(Despesas::setValor, DataType.PRICE);
            despesasGenerator.setData(Despesas::setMes, DataType.WORD);


            Random r3 = new Random(seed);
            List<Despesas> despesa = despesasGenerator.create(5, seed).stream().peek(contact -> {
                contact.setStatus(statuses.get(r3.nextInt(statuses.size())));
            }).collect(Collectors.toList());

            despesasRepository.saveAll(despesa);

            logger.info("Despesas geradas");
            
//--------------------------------------------------------------------------------------------------------
            

            ExampleDataGenerator<Receitas> receitasGenerator = new ExampleDataGenerator<>(Receitas.class,
                    LocalDateTime.now());
            receitasGenerator.setData(Receitas::setNatureza, DataType.COMPANY_NAME);
            receitasGenerator.setData(Receitas::setValor, DataType.PRICE);
            receitasGenerator.setData(Receitas::setMes, DataType.WORD);


            Random r4 = new Random(seed);
            List<Receitas> receitas = receitasGenerator.create(5, seed).stream().peek(contact -> {
                contact.setStatus(statuses.get(r4.nextInt(statuses.size())));
            }).collect(Collectors.toList());

            receitasRepository.saveAll(receitas);

            logger.info("Receitas geradas");
            
            
        };
    }

}
