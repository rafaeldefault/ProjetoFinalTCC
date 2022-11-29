package com.example.application.views.list;

import java.util.List;

import javax.annotation.security.PermitAll;

import com.example.application.data.entity.Contas;
import com.example.application.data.entity.ContasPagar;
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

@Route(value = "contaspagar", layout = MainLayout.class)
@PageTitle("Contas a Pagar")
@PermitAll
public class ContasPagarView extends VerticalLayout {
	
	Grid<ContasPagar> grid = new Grid<>(ContasPagar.class);
    TextField filterText = new TextField();
    ContasPagarForm form;
    Chart chart;
    Span stats = new Span();
    private CrmService service;
    


    public ContasPagarView(CrmService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        

        configureGrid();
        configureForm();
        
        
        

        add(
        		getContasPagarStats(),
                getToolbar(),
                getContent()
        );

        updateList(); 	
        
        
        fecharEditor();

    }
    

	private void fecharEditor() {  	
    	form.setContaPagar(null);
    	form.setVisible(false);
    	removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.buscaTodasContasPagar(filterText.getValue()));
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
        form = new ContasPagarForm(service.buscaTodosStatus());
        ((HasSize) form).setWidth("25em");

        form.addListener(ContasPagarForm.SalvarEvento.class, this::salvarContaPagar);
        form.addListener(ContasPagarForm.DeletarEvento.class, this::deletarContaPagar);
        form.addListener(ContasPagarForm.FecharEvento.class, e -> fecharEditor());

    }

    private void salvarContaPagar(ContasPagarForm.SalvarEvento event) {
    	service.salvarContaPagar(event.getContasPagar());
    	updateList();
    	fecharEditor();
    	UI.getCurrent().getPage().reload();
    }
    
    private void deletarContaPagar(ContasPagarForm.DeletarEvento event) {
    	service.deletarContaPagar(event.getContasPagar());
    	updateList();
    	fecharEditor();
    }


    private void configureGrid() {
        grid.addClassNames("contasPagar-grid");
        grid.setAllRowsVisible(true);
        grid.setColumns("entidade", "valor", "vencimento");
        grid.addColumn(contact -> contact.getStatus().getNomePagar()).setHeader("Status");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        
        
        
        grid.asSingleSelect().addValueChangeListener(e -> editarConta(e.getValue()));
    }
 
    
    private void editarConta(ContasPagar contaPagar) {
    	if(contaPagar == null) {
    		fecharEditor();
    	}else {
    		form.setContaPagar(contaPagar);
    		form.setVisible(true);
    		addClassName("editing");
    	}
    	
    }
    

    private Component getToolbar() {
        filterText.setPlaceholder("Procurar por conta...");
        filterText.setClearButtonVisible(true);
        setDefaultHorizontalComponentAlignment(Alignment.START);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContaButton = new Button("Criar conta");
        addContaButton.addClickListener(e -> adicionarConta());
        

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContaButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

	private void adicionarConta() {
		grid.asSingleSelect().clear();
		editarConta(new ContasPagar());
	}
	
	
	private Component getContasPagarStats() {
		H3 stats = new H3("Divida Total R$" + service.somaDivida());
		stats.addClassNames("text-xl", "mt-m");
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		return stats;
	}
	
	
	
	

}
