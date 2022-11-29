package com.example.application.data.entity;

import javax.persistence.Entity;

@Entity
public class Status extends AbstractEntity {
    private String nomePagar;

    public Status() {

    }

    public Status(String nomePagar) {
        this.nomePagar = nomePagar;
    }

    public String getNomePagar() {
        return nomePagar;
    }

    public void setName(String nomePagar) {
        this.nomePagar = nomePagar;
    }

}
