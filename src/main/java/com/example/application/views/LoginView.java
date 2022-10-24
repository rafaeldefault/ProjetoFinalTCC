package com.example.application.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Login | MyFIN")
public class LoginView extends VerticalLayout implements BeforeEnterListener{
	private LoginForm login = new LoginForm();
	
	public LoginView() {
		addClassName("login-view");
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		
		
		//Internacionalização do formulario de login
		
		LoginI18n i18n = LoginI18n.createDefault();
		

		LoginI18n.Form i18nForm = i18n.getForm();
		i18nForm.setTitle("Entre com seus dados");
		i18nForm.setUsername("Usuário");
		i18nForm.setPassword("Senha");
		i18nForm.setSubmit("Enviar");
		i18nForm.setForgotPassword("Esqueceu a senha ?");
		i18n.setForm(i18nForm);

		LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
		i18nErrorMessage.setTitle("Erro");
		i18nErrorMessage.setMessage("Verifique seus dados e tente novamente !");
		i18n.setErrorMessage(i18nErrorMessage);

		LoginForm loginForm = new LoginForm();
		loginForm.setI18n(i18n);
		
		
		loginForm.setAction("login");
		
		
		
		add(
				new H1("MyFIN"),
				loginForm
			);
		
			
	}
	
	
	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		if(beforeEnterEvent.getLocation().getQueryParameters()
			.getParameters()
			.containsKey("error")){
					login.setError(true);
		}
	}
	

}
