package com.example.application.data.repository;

import com.example.application.data.entity.ContasPagar;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ContasPagarRepository extends JpaRepository<ContasPagar, Integer> {

    @Query("select c from ContasPagar c " +
        "where lower(c.entidade) like lower (concat('%', :nomeConta, '%'))")
    List<ContasPagar> busca(@Param("nomeConta") String nomeConta);
    
    
    @Query(value = "SELECT sum(valor) FROM ContasPagar")
    public double soma();
    
    

}

