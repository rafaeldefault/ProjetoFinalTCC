package com.example.application.data.service;

import com.example.application.data.entity.Contas;
import com.example.application.data.entity.ContasPagar;
import com.example.application.data.entity.Despesas;
import com.example.application.data.entity.Receitas;
import com.example.application.data.entity.Status;
import com.example.application.data.entity.TipoConta;
import com.example.application.data.repository.ContasPagarRepository;
import com.example.application.data.repository.ContasRepository;
import com.example.application.data.repository.DespesasRepository;
import com.example.application.data.repository.ReceitasRepository;
import com.example.application.data.repository.StatusRepository;
import com.example.application.data.repository.TipoContaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmService {

    private final ContasRepository contasRepository;
    private final StatusRepository statusRepository;
    private final ContasPagarRepository contasPagarRepository;
    private final ReceitasRepository receitasRepository;
    private final DespesasRepository despesasRepository;
    private final TipoContaRepository tipoContaRepository;

    public CrmService(ContasRepository contasRepository,
    		ContasPagarRepository contasPagarRepository,
    		DespesasRepository despesasRepository,
    		ReceitasRepository receitasRepository,
    		StatusRepository statusRepository,
    		TipoContaRepository tipoContaRepository) {

        this.contasRepository = contasRepository;
        this.contasPagarRepository = contasPagarRepository;
        this.despesasRepository = despesasRepository;
        this.receitasRepository = receitasRepository;
        this.statusRepository = statusRepository;
        this.tipoContaRepository = tipoContaRepository;
    }
    
    //ContasBancarias

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

    public void deletarConta(Contas contas){
        contasRepository.delete(contas);
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
    
    
    //ContasPagar
    
    
    public List<ContasPagar> buscaTodasContasPagar(String filterText){
        if(filterText == null || filterText.isEmpty()){
            return contasPagarRepository.findAll();
        } else {
            return contasPagarRepository.busca(filterText);
        }
    }
    
    public void salvarContaPagar(ContasPagar contaPagar){
        if(contaPagar == null){
            System.err.println("Conta retornou null");
            return;
        }

        contasPagarRepository.save(contaPagar);
    }
    
    public double somaDivida() {
    	return contasPagarRepository.soma();
    }
    
    public void deletarContaPagar(ContasPagar contaPagar){
        contasPagarRepository.delete(contaPagar);
    }
    
    
    
    
    //Depesas
    
    public List<Despesas> buscaTodasDespesas(String filterText){
        if(filterText == null || filterText.isEmpty()){
            return despesasRepository.findAll();
        } else {
            return despesasRepository.busca(filterText);
        }
    }
    
    public void salvarDespesa(Despesas despesa){
        if(despesa == null){
            System.err.println("Despesa retornou null");
            return;
        }

        despesasRepository.save(despesa);
    }
    
    public double somaDespesas() {
    	return despesasRepository.soma();
    }
    
    public void deletarDespesa(Despesas despesa){
        despesasRepository.delete(despesa);
    }
    
    //Receitas
    
    public List<Receitas> buscaTodasReceitas(String filterText){
        if(filterText == null || filterText.isEmpty()){
            return receitasRepository.findAll();
        } else {
            return receitasRepository.busca(filterText);
        }
    }
    
    public List<Receitas> somaMesReceitas(String mesComp) {
    	return receitasRepository.buscaMes(mesComp);
    }
    
    public void salvarReceita(Receitas receita){
        if(receita == null){
            System.err.println("Receita retornou null");
            return;
        }

        receitasRepository.save(receita);
    }
    
    public double somaReceitas() {
    	return receitasRepository.soma();
    }
    
    public void deletarReceita(Receitas receita){
        receitasRepository.delete(receita);
    }

    public List<Status> buscaTodosStatus(){
        return statusRepository.findAll();
    }
    
    public List<TipoConta> buscaTodosTipos(){
        return tipoContaRepository.findAll();
    }
}
