package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import br.ufscar.dc.latosensu.aplicacaofinanceira.validation.Cep;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "endereco")
public class Endereco {
    
    @Id
    @SequenceGenerator(name="Endereco_Generator", sequenceName="endereco_sequence", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="Endereco_Generator")    
    @Column(name = "endereco_id", nullable = false)    
    private Long id;
    
    @Column(name = "logradouro", nullable = false, length = 255)
    private String logradouro;
    
    @Column(name = "numero", nullable = false)
    private int numero;
    
    @Column(name = "complemento", length = 255)
    private String complemento;
    
    @Column(name = "bairro", nullable = false, length = 255)
    private String bairro;
    
    @Cep(message = "{enderecoCepInvalido}")
    @Column(name = "cep", nullable = false, length = 9)
    private String cep;
    
    @OneToOne(mappedBy = "endereco", fetch = FetchType.LAZY)
    private Agencia agencia;
    
    @JsonIgnoreProperties({"nome", "estado", "hibernateLazyInitializer"})
    @JoinColumn(name = "cidade_id", referencedColumnName = "cidade_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cidade cidade;

    public Endereco() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }
}
