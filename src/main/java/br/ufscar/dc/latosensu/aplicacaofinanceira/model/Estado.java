package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "estado")
public class Estado {
    
    @Id
    @SequenceGenerator(name = "Estado_Generator", sequenceName = "estado_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Estado_Generator")
    @Column(name = "estado_id", nullable = false)
    private Long id;
    
    @Column(name = "nome", nullable = false, length = 255, unique = true)
    private String nome;

    public Estado() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
