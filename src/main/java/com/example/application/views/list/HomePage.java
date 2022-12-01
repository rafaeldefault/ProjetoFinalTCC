package com.example.application.views.list;

import javax.annotation.security.PermitAll;

import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.Crosshair;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.charts.model.style.GradientColor;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route(value = "", layout = MainLayout.class)
@PageTitle("Home")
@PermitAll
public class HomePage extends VerticalLayout{
	
private CrmService service;

	public HomePage(CrmService service) {
		 this.service = service;
	        addClassName("list-view");
	        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
	     add(getContaStats());
		
	}
	
	
	private Component getContaStats() {
		H2 h2 = new H2("Bem-Vindo ao MyFIN!");
		
		
		H3 stats1 = new H3("Receitas Totais ");
		stats1.addClassNames("text-xl", "mt-m");
			
		Button but1;
        but1 = new Button("R$ "+service.somaReceitas());
        but1.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        but1.setEnabled(false);
        but1.getStyle().set("background-color","rgba(0, 128, 0, 0.25)");
        but1.getStyle().set("height","50px !important");
        but1.getStyle().set("margin-top","25px");
        but1.getStyle().set("margin-bot","10px");
        but1.getStyle().set("font-size", "20px !important");
        
		H3 stats2 = new H3("Despesas Totais ");
		stats1.addClassNames("text-xl", "mt-m");
		
		
		Button but2;
        but2 = new Button("R$ "+service.somaDespesas());
        but2.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        but2.setEnabled(false);
        but2.getStyle().set("background-color","rgba(128, 0, 0, 0.25)");
        but2.getStyle().set("height","50px !important");
        but2.getStyle().set("margin-top","25px");
        but2.getStyle().set("margin-bot","10px");
        but2.getStyle().set("font-size", "20px !important");
        
		H3 stats3 = new H3("Saldo em Conta ");
		stats1.addClassNames("text-xl", "mt-m");
        
		Button but3;
        but3 = new Button("R$ "+service.somaSaldo());
        but3.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        but3.setEnabled(false);
        but3.getStyle().set("background-color","rgba(0, 0, 128, 0.25)");
        but3.getStyle().set("height","50px !important");
        but3.getStyle().set("margin-top","25px");
        but3.getStyle().set("margin-bot","10px");
        but3.getStyle().set("font-size", "20px !important");
		
		
		
		
		HorizontalLayout hor1 = new HorizontalLayout();
		HorizontalLayout hor2 = new HorizontalLayout();
		HorizontalLayout hor3 = new HorizontalLayout();
		
	
		hor1.add(stats1);
		hor1.add(but1);
		hor2.add(stats2);
		hor2.add(but2);		
		hor3.add(stats3);
		hor3.add(but3);
		
		VerticalLayout vert = new VerticalLayout();
		vert.add(h2,hor3,hor1,hor2);
		
		vert.setAlignItems(Alignment.CENTER);
		
		return vert;
	}

}
	
	


