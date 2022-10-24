package com.example.application.data.repository;

import com.example.application.data.entity.Contas;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ContasRepository extends JpaRepository<Contas, Integer> {

    @Query("select c from Contas c " +
        "where lower(c.conta) like lower (concat('%', :nomeConta, '%'))")
    List<Contas> busca(@Param("nomeConta") String nomeConta);
    
    
    @Query(value = "SELECT sum(saldo) FROM Contas")
    public double soma();
    
    

}
