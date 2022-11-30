package com.example.application.views.list;

import java.util.List;

import javax.annotation.security.PermitAll;

import com.example.application.data.entity.ContasPagar;
import com.example.application.data.entity.Despesas;
import com.example.application.data.entity.Receitas;
import com.example.application.data.entity.Status;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.style.GradientColor;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;


public class ReceitasForm extends FormLayout {
	
	FormLayout forml = new FormLayout();
	
	
	
	Binder<Receitas> binder = new BeanValidationBinder<>(Receitas.class);


    TextField natureza = new TextField("Natureza");
    NumberField valor = new NumberField("Valor");
    TextField mes = new TextField("Mês");
    
    



    Button criar = new Button("Criar");
    Button deletar = new Button("Deletar");
    Button cancelar = new Button("Cancelar");
    private Receitas receita;

    public ReceitasForm(){
        addClassName("contact-form");
        binder.bindInstanceFields(this);
        
        

        

        add(
                natureza,
                valor,
                mes,
                createButtonLayout()
        );
    }

    public void setReceita(Receitas receita){
        this.receita = receita;
        binder.readBean(receita);
    }

    
    private Component createButtonLayout(){
        criar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.MATERIAL_CONTAINED);
        deletar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.MATERIAL_CONTAINED);
        deletar.getStyle().set("background","var(--lumo-error-color)");
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        criar.addClickListener(event -> validarESalvar());
        deletar.addClickListener(event -> fireEvent(new DeletarEvento(this, receita)));
        cancelar.addClickListener(event -> fireEvent(new FecharEvento(this)));

        criar.addClickShortcut(Key.ENTER);
        cancelar.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(criar, deletar, cancelar);
    }

    private void validarESalvar() {
        try{
            binder.writeBean(receita);
            fireEvent(new SalvarEvento(this, receita));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Eventos
    public static abstract class ReceitasEvento extends ComponentEvent<ReceitasForm> {
        private Receitas receita;

        protected ReceitasEvento(ReceitasForm source, Receitas receita) {
            super(source, false);
            this.receita = receita;
        }

        public Receitas getReceitas() {
            return receita;
        }
    }

    public static class SalvarEvento extends ReceitasEvento {
        SalvarEvento(ReceitasForm source, Receitas receita) {
            super(source, receita);
        }
    }

    public static class DeletarEvento extends ReceitasEvento {
        DeletarEvento(ReceitasForm source, Receitas receita) {
            super(source, receita);
        }

    }

    public static class FecharEvento extends ReceitasEvento {
        FecharEvento(ReceitasForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
    
    
    

}