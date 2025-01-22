package com.example.kino_search.model;

import jakarta.persistence.*;

@Entity // Указывает, что этот класс является сущностью
@Table(name = "genre") // Соответствует таблице "genre" в базе данных
public class Genre {

    @Id // Указывает, что поле является первичным ключом
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Генерация значения для первичного ключа
    private int id;

    @Column(name = "name", nullable = false, unique = true, length = 20)
    // Указывает, что поле сопоставлено с колонкой "name" в базе данных
    // Уникальное значение, обязательное, ограничение на длину
    private String name;

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
