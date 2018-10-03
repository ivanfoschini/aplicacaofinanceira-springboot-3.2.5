package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Collection;
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
public class Cidade implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name = "Cidade_Generator", sequenceName = "cidade_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Cidade_Generator")
    @Column(name = "cidade_id", nullable = false)
    private Long id;
    
    @NotNull(message = "{cidadeNomeNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{cidadeNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;
    
    @JsonIgnoreProperties({"nome", "cidades", "hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "estado_id", referencedColumnName = "estado_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estado estado;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cidade", fetch = FetchType.LAZY)
    private Collection<Endereco> enderecos;

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
    
    public Collection<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(Collection<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cidade)) {
            return false;
        }
        
        Cidade other = (Cidade) object;
        
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        
        return true;
    }
}
