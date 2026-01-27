package com.example.demo.model;

import jakarta.persistence.*; // ⚠️ IMPORTANTE: jakarta.persistence (no javax)
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "cliente", schema = "dummy_data")
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "dni")
    private String dni;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "cumpleanos")
    private LocalDate cumpleanos;

    @Column(name = "email")
    private String email;
}