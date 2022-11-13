package com.example.application.views.list;

import java.util.List;

import javax.annotation.security.PermitAll;

import com.example.application.data.entity.Contas;
import com.example.application.data.entity.ContasPagar;
import com.example.application.data.entity.Despesas;
import com.example.application.data.entity.Status;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.style.GradientColor;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@Route(value = "depesas", layout = MainLayout.class)
@PageTitle("Despesas")
@PermitAll
public class DespesasView extends VerticalLayout {
	
	Grid<Despesas> grid = new Grid<>(Despesas.class);
    TextField filterText = new TextField();
    DespesasForm form;
    Chart chart;
    Span stats = new Span();
    private CrmService service;
    


    public DespesasView(CrmService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        

        configureGrid();
        configureForm();
        
        
        

        add(
        		getDespesasStats(),
                getToolbar(),
                getContent()
        );

        updateList(); 	
        
        
        fecharEditor();

    }
    

	private void fecharEditor() {  	
    	form.setDespesa(null);
    	form.setVisible(false);
    	removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.buscaTodasDespesas(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout conteudo = new HorizontalLayout(grid, form);
        conteudo.setFlexGrow(2, grid);
        conteudo.setFlexGrow(1, form);
        conteudo.addClassName("conteudo");
        conteudo.setSizeFull();

        return conteudo;
    }

    private void configureForm() {
        form = new DespesasForm();
        ((HasSize) form).setWidth("25em");

        form.addListener(DespesasForm.SalvarEvento.class, this::salvarDespesa);
        form.addListener(DespesasForm.DeletarEvento.class, this::deletarDespesa);
        form.addListener(DespesasForm.FecharEvento.class, e -> fecharEditor());

    }

    private void salvarDespesa(DespesasForm.SalvarEvento event) {
    	service.salvarDespesa(event.getDespesas());
    	updateList();
    	fecharEditor();
    	UI.getCurrent().getPage().reload();
    }
    
    private void deletarDespesa(DespesasForm.DeletarEvento event) {
    	service.deletarDespesa(event.getDespesas());
    	updateList();
    	fecharEditor();
    }


    private void configureGrid() {
        grid.addClassNames("contasPagar-grid");
        grid.setAllRowsVisible(true);
        grid.setColumns("natureza", "valor", "mes");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        
        
        
        grid.asSingleSelect().addValueChangeListener(e -> editarDespesa(e.getValue()));
    }
 
    
    private void editarDespesa(Despesas despesa) {
    	if(despesa == null) {
    		fecharEditor();
    	}else {
    		form.setDespesa(despesa);
    		form.setVisible(true);
    		addClassName("editing");
    	}
    	
    }
    

    private Component getToolbar() {
        filterText.setPlaceholder("Procurar por despesa...");
        filterText.setClearButtonVisible(true);
        setDefaultHorizontalComponentAlignment(Alignment.START);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContaButton = new Button("Criar Despesa");
        addContaButton.addClickListener(e -> adicionarDespesa());
        

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContaButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

	private void adicionarDespesa() {
		grid.asSingleSelect().clear();
		editarDespesa(new Despesas());
	}
	
	
	private Component getDespesasStats() {
		H3 stats = new H3("Divida Total R$" + service.somaDivida());
		stats.addClassNames("text-xl", "mt-m");
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		return stats;
	}
	
	
	
	

}