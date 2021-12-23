package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
            
    @Column(name = "senha", nullable = false, length = 32)        
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