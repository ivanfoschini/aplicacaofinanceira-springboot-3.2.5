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
