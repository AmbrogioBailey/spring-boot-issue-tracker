package com.ambrogio.issuetracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "TITLE IS REQUIRED!")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "DESCRIPTION IS REQUIRED!")
    @Column(nullable = false, length = 2000)
    private String description;



    public Issue() {}

    public Issue(String title, String description) {
        this.title = title;
        this.description = description;

    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(Long id) { this.id = id; }

    public void setDescription(String description) {
        this.description = description;
    }

}
