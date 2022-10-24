package com.example.application.views.list;

import javax.annotation.security.PermitAll;

import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.charts.model.style.GradientColor;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "contaspagar", layout = MainLayout.class)
@PageTitle("Contas a Pagar")
@PermitAll
public class ContasPagarView extends VerticalLayout {
	
	private CrmService service;
	
	public ContasPagarView(CrmService service) {
		this.service = service;
		addClassName("contaspagar-view");
		GradientColor color = GradientColor.createLinear(0, 0, 0, 1);
		color.addColorStop(0, new SolidColor("#000000"));
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
	}

}
