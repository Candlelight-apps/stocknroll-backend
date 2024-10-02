package com.candlelightapps.stocknroll_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    Long id;

    @Column(updatable = false, nullable = false)
    Long recipeId;

    @Column(nullable = false)
    String title;

    @Column
    int readyInMinutes;

    @Column
    String sourceUrl;

    @Column
    String image;

    @Column
    boolean favourite;

}
