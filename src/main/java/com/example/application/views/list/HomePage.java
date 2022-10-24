package com.example.application.views.list;

import javax.annotation.security.PermitAll;

import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.style.GradientColor;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
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
		getContaStats();
	}
	
	private Component getContaStats() {
		H3 stats = new H3("Saldo Total R$");
		stats.addClassNames("text-xl", "mt-m");
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		return stats;
	}
	
	

}
