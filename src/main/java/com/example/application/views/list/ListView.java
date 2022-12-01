package com.example.application.views.list;

import com.example.application.data.entity.Contas;
import com.example.application.data.repository.ContasRepository;
import com.example.application.data.repository.TipoContaRepository;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.style.GradientColor;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginI18n.Form;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.server.frontend.TaskRunNpmInstall.Stats;

import java.util.Collections;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.DisposableBean;

@Route(value = "listview", layout = MainLayout.class)
@PageTitle("Contas Correntes")
@PermitAll
public class ListView extends VerticalLayout {
    Grid<Contas> grid = new Grid<>(Contas.class);
    TextField filterText = new TextField();
    ContaForm form;
    Chart chart;
    Span stats = new Span();
    
    private CrmService service;
    


    public ListView(CrmService service) {
        this.service = service;
        addClassName("list-view");
        
        
        

        configureGrid();
        configureForm();
        
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        

        add(
        		getContaStats(),
                getToolbar(),
                getContent()
        );

        updateList(); 	
        
        
        fecharEditor();

    }
    

	private void fecharEditor() {  	
    	form.setConta(null);
    	form.setVisible(false);
    	removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.buscaTodasContas(filterText.getValue()));
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
        form = new ContaForm(service.buscaTodosTipos());
        ((HasSize) form).setWidth("30em");

        form.addListener(ContaForm.SalvarEvento.class, this::salvarConta);
        form.addListener(ContaForm.DeletarEvento.class, this::deletarConta);
        form.addListener(ContaForm.FecharEvento.class, e -> fecharEditor());

    }

    private void salvarConta(ContaForm.SalvarEvento event) {
    	service.salvarConta(event.getContas());
    	updateList();
    	fecharEditor();
    	UI.getCurrent().getPage().reload();
    }
    
    private void deletarConta(ContaForm.DeletarEvento event) {
    	service.deletarConta(event.getContas());
    	updateList();
    	fecharEditor();
    	UI.getCurrent().getPage().reload();
    }


    private void configureGrid() {
        grid.addClassNames("contas-grid");
        grid.setAllRowsVisible(true);
        grid.setColumns("conta", "saldo");
        grid.addColumn(contact -> contact.getTipoConta().getName()).setHeader("Tipo");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        
        
        
        grid.asSingleSelect().addValueChangeListener(e -> editarConta(e.getValue()));
    }
 
    
    private void editarConta(Contas conta) {
    	if(conta == null) {
    		fecharEditor();
    	}else {
    		form.setConta(conta);
    		form.setVisible(true);
    		addClassName("editing");
    	}
    	
    }
    

    private Component getToolbar() {
        filterText.setPlaceholder(" Procurar por conta...");
        filterText.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContaButton = new Button("Criar Conta", new Icon(VaadinIcon.PLUS));
        addContaButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);

        addContaButton.addClickListener(e -> adicionarConta());
        
        Button gerarGraficoConta = new Button("Gerar Dashboard", new Icon(VaadinIcon.PIE_CHART));
        gerarGraficoConta.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.MATERIAL_CONTAINED);
        gerarGraficoConta.getStyle().set("background","var(--lumo-primary-color)");
        gerarGraficoConta.addClickListener(g ->
        										gerarGraficoConta.getUI().ifPresent(ui ->
        										ui.navigate("dashboard")));
        

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContaButton, gerarGraficoConta);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

	private void adicionarConta() {
		grid.asSingleSelect().clear();
		editarConta(new Contas());
	}
	
	
	private Component getContaStats() {
		Button but;
		String valor;
		
		valor = "R$: " + service.somaSaldo();
		
        if (service.somaSaldo() > 0) {
	        but = new Button(valor);
	        but.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
	        but.setEnabled(false);
	        but.getStyle().set("background-color","rgba(0, 128, 0, 0.2)");
	        but.getStyle().set("height","50px !important");
	        but.getStyle().set("margin-top","25px");
	        but.getStyle().set("margin-bot","10px");
	        but.getStyle().set("font-size", "20px !important");
        } else {
	        but = new Button(valor);
	        but.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
	        but.setEnabled(false);
	        but.getStyle().set("background-color","rgba(128, 0, 0, 0.2)");
	        but.getStyle().set("height","50px !important");
	        but.getStyle().set("margin-top","25px");
	        but.getStyle().set("margin-bot","10px");
	        but.getStyle().set("font-size", "20px !important");
        }
		
		
		HorizontalLayout hor = new HorizontalLayout();
		H3 stats = new H3("Saldo Total: ");
		
		
		hor.add(stats);
		hor.add(but);
		return hor;
	}
	



}