package com.alex.datajpa.app.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "authorities", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "authority"})}) //id no repeat autho
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincremental
    private Long id;

    private String authority;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    // Eclipse pide esto, pero vscode no lo sugiere
    private static final long serialVersionUID = 1L;
}
