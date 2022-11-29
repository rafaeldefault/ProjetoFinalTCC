package com.example.application.views.list;

import javax.annotation.security.PermitAll;

import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataLabelsFunnel;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.charts.model.PlotLine;
import com.vaadin.flow.component.charts.model.PlotOptionsColumn;
import com.vaadin.flow.component.charts.model.PlotOptionsFunnel;
import com.vaadin.flow.component.charts.model.Stacking;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.charts.model.style.GradientColor;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Gráficos")
@PermitAll
public class DashBoardView extends VerticalLayout{
	private CrmService service;
	
	
	public DashBoardView(CrmService service) {
		this.service = service;
		addClassName("dashboard-view");
		GradientColor color = GradientColor.createLinear(0, 0, 0, 1);
		color.addColorStop(0, new SolidColor("#000000"));
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		add(getContaStats(), getContasSaldoChart());
	}
	
	private Component getContaStats() {
		H3 stats = new H3("Saldo Total " + service.somaSaldo());
		stats.addClassNames("text-xl", "mt-m");
		return stats;
	}



	private Component getContasSaldoChart() {
		Chart chart = new Chart(ChartType.PIE);
		
		// Modify the default configuration a bit
		Configuration conf = chart.getConfiguration();
		conf.getLegend().setEnabled(true); 
        

		// Give more room for the labels
		conf.getChart().setSpacingRight(0);

		// Configure the funnel neck shape
		PlotOptionsFunnel options = new PlotOptionsFunnel();
		

		// Style the data labels
		DataLabelsFunnel dataLabels = new DataLabelsFunnel();
		dataLabels.setFormat("<b>{point.name}</b> ({point.y:,.2f})");
		dataLabels.setSoftConnector(true);
		dataLabels.setColor(SolidColor.BLACK);
		options.setDataLabels(dataLabels);
		
        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter(
                "function() { return 'R$: '+ this.y +'';}");
        tooltip.setShared(false);
        conf.setTooltip(tooltip);

		conf.setPlotOptions(options);
		
		
		DataSeries dataSeries = new DataSeries();
		service.buscaTodasContas(null).forEach(conta->{
			dataSeries.add(new DataSeriesItem(conta.getConta(), conta.getSaldo()));
			});
		chart.getConfiguration().setSeries(dataSeries);
		
		return chart;
	}

}
