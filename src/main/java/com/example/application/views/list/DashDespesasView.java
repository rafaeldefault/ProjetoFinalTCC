package com.example.application.views.list;

import java.util.List;

import javax.annotation.security.PermitAll;

import com.example.application.data.entity.Despesas;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AxisTitle;
import com.vaadin.flow.component.charts.model.AxisType;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.Credits;
import com.vaadin.flow.component.charts.model.Cursor;
import com.vaadin.flow.component.charts.model.DataLabels;
import com.vaadin.flow.component.charts.model.DataLabelsFunnel;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.HorizontalAlign;
import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.charts.model.LayoutDirection;
import com.vaadin.flow.component.charts.model.Legend;
import com.vaadin.flow.component.charts.model.PlotLine;
import com.vaadin.flow.component.charts.model.PlotOptionsBar;
import com.vaadin.flow.component.charts.model.PlotOptionsColumn;
import com.vaadin.flow.component.charts.model.PlotOptionsFunnel;
import com.vaadin.flow.component.charts.model.Stacking;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.VerticalAlign;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.charts.model.style.GradientColor;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route(value = "dashboard_despesas", layout = MainLayout.class)
@PageTitle("Gráficos")
@PermitAll
public class DashDespesasView extends VerticalLayout{
	private CrmService service;
	
	
	public DashDespesasView(CrmService service) {
		this.service = service;
		addClassName("dashboard-view");
		GradientColor color = GradientColor.createLinear(0, 0, 0, 1);
		color.addColorStop(0, new SolidColor("#000000"));
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		add(getDespesasStats(), getDespesasSaldoChart());
	}
	
	private Component getDespesasStats() {
		H3 stats = new H3("Despesa Total R$" + service.somaDespesas());
		stats.addClassNames("text-xl", "mt-m");
		return stats;
	}



	private Component getDespesasSaldoChart() {
		Chart chart = new Chart(ChartType.COLUMN);
		
		// Modify the default configuration a bit
		Configuration conf = chart.getConfiguration();
		
        conf.setTitle("Despesas");
        conf.setSubTitle("Gastos registrados");
        conf.getLegend().setEnabled(false);

        XAxis x = new XAxis();
        x.setType(AxisType.CATEGORY);
        conf.addxAxis(x);

        YAxis y = new YAxis();
        y.setTitle("Reais (BRL)");
        conf.addyAxis(y);
        

        PlotOptionsColumn column = new PlotOptionsColumn();
        
        DataLabels dica = new DataLabels();
        dica.setFormatter(
        		"function() { return 'R$: '+ this.y +'';}"
        );
        dica.setEnabled(true);
        
        column.setDataLabels(dica);
       
        conf.setPlotOptions(column);


        PlotOptionsColumn plotOptionsColumn = new PlotOptionsColumn();
        plotOptionsColumn.setColor(SolidColor.INDIANRED);

		// Give more room for the labels
		conf.getChart().setSpacingRight(0);
		
        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter(
                "function() { return 'R$: '+ this.y +'';}");
        tooltip.setShared(false);
        conf.setTooltip(tooltip);

		conf.addPlotOptions(plotOptionsColumn);
		
		
		DataSeries dataSeries = new DataSeries();
		service.buscaTodasDespesas(null).forEach(despesa->{
			dataSeries.add(new DataSeriesItem(despesa.getMes(), service.somaMesDespesas(despesa.getMes())));
		});
		
		chart.getConfiguration().setSeries(dataSeries);
		
				

        add(chart);
		
		return chart;
	}

}
