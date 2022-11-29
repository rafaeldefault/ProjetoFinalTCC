package com.example.application.views;

import com.example.application.security.SecurityService;
import com.example.application.views.list.ContasPagarView;
import com.example.application.views.list.DashBoardView;
import com.example.application.views.list.DespesasView;
import com.example.application.views.list.HomePage;
import com.example.application.views.list.ListView;
import com.example.application.views.list.ReceitasView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("app-layout-navbar")
public class MainLayout extends AppLayout {


	  public MainLayout(SecurityService securityService) {
		
	    H1 title = new H1("MyFIN");
	    title.getStyle()
	      .set("font-size", "var(--lumo-font-size-l)")
	      .set("left", "var(--lumo-space-l)")
	      .set("margin", "0")
	      .set("position", "absolute");
	
	    Tabs tabs = getTabs();
	    
		Button logout = new Button("Sair", e -> securityService.logout());
		logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
	
	    addToNavbar(title, tabs, logout);
	  }
	
	  private Tabs getTabs() {
	    Tabs tabs = new Tabs();
	    tabs.getStyle().set("margin", "auto");
	    tabs.add(
	      createTab("Bem-Vindo", HomePage.class),
	      createTab("Contas Bancárias", ListView.class),
	      createTab("Receitas", ReceitasView.class),
	      createTab("Despesas", DespesasView.class),
	      createTab("Boletos", ContasPagarView.class)
	    );
	    return tabs;
	  }
	
	  private Tab createTab(String viewName, Class<? extends Component> navigationTarget) {
	    RouterLink link = new RouterLink(viewName, navigationTarget);
	   
	    link.add();
	    // Demo has no routes
	
	    link.setTabIndex(-1);
	
	    return new Tab(link);
	  }
	}


