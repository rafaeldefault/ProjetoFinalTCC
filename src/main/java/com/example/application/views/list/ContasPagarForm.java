package com.example.application.views.list;

import java.util.List;

import javax.annotation.security.PermitAll;

import com.example.application.data.entity.ContasPagar;
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


public class ContasPagarForm extends FormLayout {
	
	Binder<ContasPagar> binder = new BeanValidationBinder<>(ContasPagar.class);


    TextField entidade = new TextField("Entidade");
    NumberField valor = new NumberField("Valor");
    ComboBox<Status> status = new ComboBox<Status>("Status");      
    DatePicker vencimento = new DatePicker("Vencimento");
    
    



    Button criar = new Button("Criar");
    Button deletar = new Button("Deletar");
    Button cancelar = new Button("Cancelar");
    private ContasPagar contasPagar;

    public ContasPagarForm(List<Status> statuses){
        addClassName("contact-form");
        binder.bindInstanceFields(this);
        

        status.setItems(statuses);
        status.setItemLabelGenerator(Status::getNomePagar);

        add(
                entidade,
                valor,
                status,
                vencimento,
                createButtonLayout()
        );
    }

    public void setContaPagar(ContasPagar contasPagar){
        this.contasPagar = contasPagar;
        binder.readBean(contasPagar);
    }

    private Component createButtonLayout(){
        criar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.MATERIAL_CONTAINED);
        deletar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.MATERIAL_CONTAINED);
        deletar.getStyle().set("background","var(--lumo-error-color)");
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        criar.addClickListener(event -> validarESalvar());
        deletar.addClickListener(event -> fireEvent(new DeletarEvento(this, contasPagar)));
        cancelar.addClickListener(event -> fireEvent(new FecharEvento(this)));

        criar.addClickShortcut(Key.ENTER);
        cancelar.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(criar, deletar, cancelar);
    }

    private void validarESalvar() {
        try{
            binder.writeBean(contasPagar);
            fireEvent(new SalvarEvento(this, contasPagar));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Eventos
    public static abstract class ContasPagarEvento extends ComponentEvent<ContasPagarForm> {
        private ContasPagar contasPagar;

        protected ContasPagarEvento(ContasPagarForm source, ContasPagar contasPagar) {
            super(source, false);
            this.contasPagar = contasPagar;
        }

        public ContasPagar getContasPagar() {
            return contasPagar;
        }
    }

    public static class SalvarEvento extends ContasPagarEvento {
        SalvarEvento(ContasPagarForm source, ContasPagar contasPagar) {
            super(source, contasPagar);
        }
    }

    public static class DeletarEvento extends ContasPagarEvento {
        DeletarEvento(ContasPagarForm source, ContasPagar contasPagar) {
            super(source, contasPagar);
        }

    }

    public static class FecharEvento extends ContasPagarEvento {
        FecharEvento(ContasPagarForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
    
    
    

}

