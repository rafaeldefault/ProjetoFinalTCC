package com.example.application.views.list;

import java.util.List;

import javax.annotation.security.PermitAll;

import com.example.application.data.entity.ContasPagar;
import com.example.application.data.entity.Despesas;
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


public class DespesasForm extends FormLayout {
	
	Binder<Despesas> binder = new BeanValidationBinder<>(Despesas.class);


    TextField natureza = new TextField("Natureza");
    NumberField valor = new NumberField("Valor");
    TextField mes = new TextField("Mês");
    
    



    Button criar = new Button("Criar");
    Button deletar = new Button("Deletar");
    Button cancelar = new Button("Cancelar");
    private Despesas despesa;

    public DespesasForm(){
        addClassName("contact-form");
        binder.bindInstanceFields(this);
        

        

        add(
                natureza,
                valor,
                mes,
                createButtonLayout()
        );
    }

    public void setDespesa(Despesas despesa){
        this.despesa = despesa;
        binder.readBean(despesa);
    }

    private Component createButtonLayout(){
        criar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deletar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        criar.addClickListener(event -> validarESalvar());
        deletar.addClickListener(event -> fireEvent(new DeletarEvento(this, despesa)));
        cancelar.addClickListener(event -> fireEvent(new FecharEvento(this)));

        criar.addClickShortcut(Key.ENTER);
        cancelar.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(criar, deletar, cancelar);
    }

    private void validarESalvar() {
        try{
            binder.writeBean(despesa);
            fireEvent(new SalvarEvento(this, despesa));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Eventos
    public static abstract class DespesasEvento extends ComponentEvent<DespesasForm> {
        private Despesas despesa;

        protected DespesasEvento(DespesasForm source, Despesas despesa) {
            super(source, false);
            this.despesa = despesa;
        }

        public Despesas getDespesas() {
            return despesa;
        }
    }

    public static class SalvarEvento extends DespesasEvento {
        SalvarEvento(DespesasForm source, Despesas despesa) {
            super(source, despesa);
        }
    }

    public static class DeletarEvento extends DespesasEvento {
        DeletarEvento(DespesasForm source, Despesas despesa) {
            super(source, despesa);
        }

    }

    public static class FecharEvento extends DespesasEvento {
        FecharEvento(DespesasForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
    
    
    

}