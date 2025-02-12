package com.example.users.domain.model;

import java.time.LocalDate;

public class User {

    private Long id;

    private String email;

    private String password;

    private String rol;

    private String name;

    private String last_name;

    private String document_number;

    private String phone;

    private LocalDate date_birth;

    public User() {
    }

    public User(String email, Long id, String password, String rol, String name, String last_name, String document_number, String phone, LocalDate date_birth) {
        this.email = email;
        this.id = id;
        this.password = password;
        this.rol = rol;
        this.name = name;
        this.last_name = last_name;
        this.document_number = document_number;
        this.phone = phone;
        this.date_birth = date_birth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDocument_number() {
        return document_number;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDate_birth() {
        return date_birth;
    }

    public void setDate_birth(LocalDate date_birth) {
        this.date_birth = date_birth;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", rol='" + rol + '\'' +
                ", name='" + name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", document_number='" + document_number + '\'' +
                ", phone='" + phone + '\'' +
                ", date_birth=" + date_birth +
                '}';
    }


}
