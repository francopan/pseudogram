package com.example.pseudogram.model;

import java.util.Objects;

public class Foto {

    private Integer id;
    private String titulo;
    private String descricao;
    private String path;

    public Foto() {
    }

    public Foto(String titulo, String descricao, String preco) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.path = preco;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Foto foto = (Foto) o;
        return id == foto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
