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
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        

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
        ((HasSize) form).setWidth("30em");

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
        filterText.setPlaceholder(" Procurar por despesa...");
        filterText.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContaButton = new Button("Criar Despesa", new Icon(VaadinIcon.PLUS));
        addContaButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.MATERIAL_CONTAINED);
        addContaButton.addClickListener(e -> adicionarDespesa());
        
        
        Button gerarGraficoDesp = new Button("Gerar Gráfico", new Icon(VaadinIcon.BAR_CHART_H));
        gerarGraficoDesp.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.MATERIAL_CONTAINED);
        gerarGraficoDesp.getStyle().set("background","var(--lumo-primary-color)");
        
        
        gerarGraficoDesp.addClickListener(g ->
        										gerarGraficoDesp.getUI().ifPresent(ui ->
        										ui.navigate("dashboard_despesas")));
        

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContaButton, gerarGraficoDesp);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

	private void adicionarDespesa() {
		grid.asSingleSelect().clear();
		editarDespesa(new Despesas());
	}
	
	
	
	private Component getDespesasStats() {
		Button but;
		String valor;
		
		valor = "R$ " + service.somaDespesas();
		

	        but = new Button(valor);
	        but.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
	        but.setEnabled(false);
	        but.getStyle().set("background-color","rgba(128, 0, 0, 0.2)");
	        but.getStyle().set("height","50px !important");
	        but.getStyle().set("margin-top","25px");
	        but.getStyle().set("margin-bot","10px");
	        but.getStyle().set("font-size", "20px !important");
        
		
		
		HorizontalLayout hor = new HorizontalLayout();
		H3 stats = new H3("Dívida Total ");
		
		
		hor.add(stats);
		hor.add(but);
		return hor;
	}
	
	

}