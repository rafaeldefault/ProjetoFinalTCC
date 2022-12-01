package com.example.application.views.list;

import com.example.application.data.entity.Contas;
import com.example.application.data.entity.TipoConta;
import com.example.application.data.entity.Status;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class ContaForm extends FormLayout {
    Binder<Contas> binder = new BeanValidationBinder<>(Contas.class);


    TextField conta = new TextField("Conta");
    NumberField saldo = new NumberField("Saldo");
    ComboBox<TipoConta> tipoConta = new ComboBox<TipoConta>("Tipo");



    Button criar = new Button("Criar", new Icon(VaadinIcon.PLUS));
    Button deletar = new Button("Deletar", new Icon(VaadinIcon.TRASH));
    Button cancelar = new Button("Cancelar", new Icon(VaadinIcon.CLOSE));
    private Contas contas;

    public ContaForm(List<TipoConta> tiposContas){
        addClassName("contact-form");
        binder.bindInstanceFields(this);

        tipoConta.setItems(tiposContas);
        tipoConta.setItemLabelGenerator(TipoConta::getName);

        add(
                conta,
                saldo,
                tipoConta,
                createButtonLayout()
        );
    }

    public void setConta(Contas contas){
        this.contas = contas;
        binder.readBean(contas);
    }

    private Component createButtonLayout(){
        criar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.MATERIAL_CONTAINED);
        deletar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.MATERIAL_CONTAINED);
        deletar.getStyle().set("background","var(--lumo-error-color)");
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        criar.addClickListener(event -> validarESalvar());
        deletar.addClickListener(event -> fireEvent(new DeletarEvento(this, contas)));
        cancelar.addClickListener(event -> fireEvent(new FecharEvento(this)));

        criar.addClickShortcut(Key.ENTER);
        cancelar.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(criar, deletar, cancelar);
    }

    private void validarESalvar() {
        try{
            binder.writeBean(contas);
            fireEvent(new SalvarEvento(this, contas));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Eventos
    public static abstract class ContaFormEvento extends ComponentEvent<ContaForm> {
        private Contas contas;

        protected ContaFormEvento(ContaForm source, Contas contas) {
            super(source, false);
            this.contas = contas;
        }

        public Contas getContas() {
            return contas;
        }
    }

    public static class SalvarEvento extends ContaFormEvento {
        SalvarEvento(ContaForm source, Contas contas) {
            super(source, contas);
        }
    }

    public static class DeletarEvento extends ContaFormEvento {
        DeletarEvento(ContaForm source, Contas contas) {
            super(source, contas);
        }

    }

    public static class FecharEvento extends ContaFormEvento {
        FecharEvento(ContaForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}






  








