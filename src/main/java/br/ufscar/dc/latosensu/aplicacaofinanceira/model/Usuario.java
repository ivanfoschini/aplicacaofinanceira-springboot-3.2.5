package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {
    
    @Id
    @SequenceGenerator(name = "Usuario_Generator", sequenceName = "usuario_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Usuario_Generator")
    @Column(name = "usuario_id", nullable = false)
    private Long id;
    
    @Column(name = "nomeDeUsuario", nullable = false, unique = true, length = 255)
    private String nomeDeUsuario;
            
    @Column(name = "senha", nullable = false, length = 60)
    private String senha;
            
    @ManyToMany(mappedBy="usuarios", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Papel> papeis;
    
    public Usuario() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeDeUsuario() {
        return nomeDeUsuario;
    }

    public void setNomeDeUsuario(String nomeDeUsuario) {
        this.nomeDeUsuario = nomeDeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Papel> getPapeis() {
        return papeis;
    }

    public void setPapeis(List<Papel> papeis) {
        this.papeis = papeis;
    }
}