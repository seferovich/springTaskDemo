package com.example.demoapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

    @Entity
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "tasks")
    public class Task {
        @Id
        @SequenceGenerator(
                name = "customer_id_sequence",
                sequenceName = "customer_id_sequence",
                allocationSize = 1
        )
        @GeneratedValue(
                strategy = GenerationType.SEQUENCE,
                generator = "customer_id_sequence"
        )
        private Integer id;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private String description;

        @Column(nullable = false)
        private Boolean finished;

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

    }
