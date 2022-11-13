package com.example.application.data.repository;

import com.example.application.data.entity.Receitas;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ReceitasRepository extends JpaRepository<Receitas, Integer> {

    @Query("select c from Receitas c " +
        "where lower(c.natureza) like lower (concat('%', :nomeConta, '%'))")
    List<Receitas> busca(@Param("nomeConta") String nomeConta);
    
    
    @Query(value = "SELECT sum(valor) FROM Receitas")
    public double soma();
    
    

}
