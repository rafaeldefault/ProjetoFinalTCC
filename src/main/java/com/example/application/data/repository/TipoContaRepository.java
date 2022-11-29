package com.example.application.data.repository;

import com.example.application.data.entity.TipoConta;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoContaRepository extends JpaRepository<TipoConta, UUID> {


}
