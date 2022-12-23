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
@Table(name = "servico")
public class Servico {
    
    @Id
    @SequenceGenerator(name = "Servico_Generator", sequenceName = "servico_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Servico_Generator")
    @Column(name = "servico_id", nullable = false)
    private Long id;
    
    @Column(name = "uri", nullable = false, unique = true, length = 255)
    private String uri;
    
    @ManyToMany(mappedBy="servicos", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Papel> papeis;

    public Servico() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<Papel> getPapeis() {
        return papeis;
    }

    public void setPapeis(List<Papel> papeis) {
        this.papeis = papeis;
    }
}
