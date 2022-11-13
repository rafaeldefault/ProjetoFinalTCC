package com.example.application.data.repository;

import com.example.application.data.entity.Despesas;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DespesasRepository extends JpaRepository<Despesas, Integer> {

    @Query("select c from Despesas c " +
        "where lower(c.natureza) like lower (concat('%', :nomeConta, '%'))")
    List<Despesas> busca(@Param("nomeConta") String nomeConta);
    
    
    @Query(value = "SELECT sum(valor) FROM Despesas")
    public double soma();
    
    

}
