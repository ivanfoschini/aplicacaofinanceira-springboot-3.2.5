package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "papel")
public class Papel {
    
    @Id
    @SequenceGenerator(name = "Papel_Generator", sequenceName = "papel_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Papel_Generator")
    @Column(name = "papel_id", nullable = false)
    private Long id;
    
    @Column(name = "nome", nullable = false, unique = true, length = 255)
    private String nome;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="usuario_papel",
            joinColumns={@JoinColumn(name="papel_id")},
            inverseJoinColumns={@JoinColumn(name="usuario_id")})
    private List<Usuario> usuarios;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="papel_servico",
            joinColumns={@JoinColumn(name="papel_id")},
            inverseJoinColumns={@JoinColumn(name="servico_id")})
    private List<Servico> servicos;

    public Papel() {}

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

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }
}
