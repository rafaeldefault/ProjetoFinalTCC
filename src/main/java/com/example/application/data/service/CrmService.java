package com.example.application.data.service;

import com.example.application.data.entity.Contas;
import com.example.application.data.entity.Status;
import com.example.application.data.repository.ContasRepository;
import com.example.application.data.repository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmService {

    private final ContasRepository contasRepository;
    private final StatusRepository statusRepository;

    public CrmService(ContasRepository contasRepository,
                      StatusRepository statusRepository) {

        this.contasRepository = contasRepository;
        this.statusRepository = statusRepository;
    }

    public List<Contas> buscaTodasContas(String filterText){
        if(filterText == null || filterText.isEmpty()){
            return contasRepository.findAll();
        } else {
            return contasRepository.busca(filterText);
        }
    }

    public long countContas(){
        return contasRepository.count();
    }

    public void deletarConta(Contas conta){
        contasRepository.delete(conta);
    }
    
    public double somaSaldo() {
    	
    	return contasRepository.soma();
    }
    

    public void salvarConta(Contas conta){
        if(conta == null){
            System.err.println("Conta retornou null");
            return;
        }

        contasRepository.save(conta);
    }

    public List<Status> buscaTodosStatus(){
        return statusRepository.findAll();
    }
}
