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
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        

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
        ((HasSize) form).setWidth("30em");

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
    	
        filterText.setPlaceholder(" Procurar por receita...");
        filterText.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filterText.setClearButtonVisible(true);
        filterText.addValueChangeListener(e -> updateList());

        Button addContaButton = new Button("Criar Receita", new Icon(VaadinIcon.PLUS));
        addContaButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        addContaButton.addClickListener(e -> adicionarReceita());
        
        Button gerarGraficoRec = new Button("Gerar Gráfico", new Icon(VaadinIcon.BAR_CHART_H));
        gerarGraficoRec.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.MATERIAL_CONTAINED);
        gerarGraficoRec.getStyle().set("background","var(--lumo-primary-color)");
        gerarGraficoRec.addClickListener(g ->
        										gerarGraficoRec.getUI().ifPresent(ui ->
        										ui.navigate("dashboard_receitas")));
        

        

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContaButton, gerarGraficoRec);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

	private void adicionarReceita() {
		grid.asSingleSelect().clear();
		editarReceita(new Receitas());
	}
	
	
	
	private Component getReceitasStats() {
		Button but;
		String valor;
		
		valor = "R$ " + service.somaDespesas();
		

	        but = new Button(valor);
	        but.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
	        but.setEnabled(false);
	        but.getStyle().set("background-color","rgba(0, 128, 0, 0.2)");
	        but.getStyle().set("height","50px !important");
	        but.getStyle().set("margin-top","25px");
	        but.getStyle().set("margin-bot","10px");
	        but.getStyle().set("font-size", "20px !important");
        
		
		
		HorizontalLayout hor = new HorizontalLayout();
		H3 stats = new H3("Receita Total ");
		
		
		hor.add(stats);
		hor.add(but);
		return hor;
	}
	
	
	
	

}
