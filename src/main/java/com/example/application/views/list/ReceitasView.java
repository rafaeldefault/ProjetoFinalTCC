package com.example.application.views.list;

import java.util.List;

import javax.annotation.security.PermitAll;

import com.example.application.data.entity.Contas;
import com.example.application.data.entity.ContasPagar;
import com.example.application.data.entity.Despesas;
import com.example.application.data.entity.Receitas;
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

@Route(value = "receitas", layout = MainLayout.class)
@PageTitle("Receitas")
@PermitAll
public class ReceitasView extends VerticalLayout {
	
	Grid<Receitas> grid = new Grid<>(Receitas.class);
    TextField filterText = new TextField();
    ReceitasForm form;
    Chart chart;
    Span stats = new Span();
    private CrmService service;
    


    public ReceitasView(CrmService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        

        configureGrid();
        configureForm();
        
        
        

        add(
        		getReceitasStats(),
                getToolbar(),
                getContent()
        );

        updateList(); 	
        
        
        fecharEditor();

    }
    

	private void fecharEditor() {  	
    	form.setReceita(null);
    	form.setVisible(false);
    	removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.buscaTodasReceitas(filterText.getValue()));
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
        form = new ReceitasForm();
        ((HasSize) form).setWidth("25em");

        form.addListener(ReceitasForm.SalvarEvento.class, this::salvarReceita);
        form.addListener(ReceitasForm.DeletarEvento.class, this::deletarReceita);
        form.addListener(ReceitasForm.FecharEvento.class, e -> fecharEditor());

    }

    private void salvarReceita(ReceitasForm.SalvarEvento event) {
    	service.salvarReceita(event.getReceitas());
    	updateList();
    	fecharEditor();
    	UI.getCurrent().getPage().reload();
    }
    
    private void deletarReceita(ReceitasForm.DeletarEvento event) {
    	service.deletarReceita(event.getReceitas());
    	updateList();
    	fecharEditor();
    }


    private void configureGrid() {
        grid.addClassNames("receitas-grid");
        grid.setAllRowsVisible(true);
        grid.setColumns("natureza", "valor", "mes");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        
        
        
        grid.asSingleSelect().addValueChangeListener(e -> editarReceita(e.getValue()));
    }
 
    
    private void editarReceita(Receitas receita) {
    	if(receita == null) {
    		fecharEditor();
    	}else {
    		form.setReceita(receita);
    		form.setVisible(true);
    		addClassName("editing");
    	}
    	
    }
    

    private Component getToolbar() {
        filterText.setPlaceholder("Procurar por receita...");
        filterText.setClearButtonVisible(true);
        setDefaultHorizontalComponentAlignment(Alignment.START);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContaButton = new Button("Criar Receita");
        addContaButton.addClickListener(e -> adicionarReceita());
        
        Button gerarGraficoRec = new Button("Gerar Gráfico");
        gerarGraficoRec.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_SUCCESS);
        gerarGraficoRec.addClickListener(g ->
        										gerarGraficoRec.getUI().ifPresent(ui ->
        										ui.navigate("dashboard_receitas")));
        
        Button gerarGraficoRecXDesp = new Button("Comparar c/ Despesas");
        gerarGraficoRecXDesp.addThemeVariants(ButtonVariant.LUMO_SUCCESS,
                ButtonVariant.LUMO_PRIMARY);
        gerarGraficoRecXDesp.addClickListener(g ->
        gerarGraficoRecXDesp.getUI().ifPresent(ui ->
        										ui.navigate("dashboard_receitasxdespesas")));
        

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContaButton, gerarGraficoRec, gerarGraficoRecXDesp);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

	private void adicionarReceita() {
		grid.asSingleSelect().clear();
		editarReceita(new Receitas());
	}
	
	
	private Component getReceitasStats() {
		H3 stats = new H3("Receita Total R$" + service.somaReceitas());
		stats.addClassNames("text-xl", "mt-m");
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		return stats;
	}
	
	
	
	

}
