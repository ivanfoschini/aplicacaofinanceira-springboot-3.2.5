package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cidade")
public class Cidade {
    
    @Id
    @SequenceGenerator(name = "Cidade_Generator", sequenceName = "cidade_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Cidade_Generator")
    @Column(name = "cidade_id", nullable = false)
    private Long id;
    
    @NotNull(message = "{cidadeNomeNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{cidadeNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;
    
    @JsonIgnoreProperties({"nome", "cidades", "hibernateLazyInitializer"})
    @JoinColumn(name = "estado_id", referencedColumnName = "estado_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estado estado;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cidade", fetch = FetchType.LAZY)
    private List<Endereco> enderecos;

    public Cidade() {}

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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }
}
