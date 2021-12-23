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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "agencia")
public class Agencia {
    
    @Id
    @SequenceGenerator(name="Agencia_Generator", sequenceName="agencia_sequence", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="Agencia_Generator")
    @Column(name = "agencia_id", nullable = false)
    private Long id;
    
    @NotNull(message = "{agenciaNumeroNaoPodeSerNulo}")
    @Min(value = 1, message = "{agenciaNumeroDeveSerMaiorDoQueZero}")
    @Column(name = "numero", nullable = false, unique = true)
    private int numero;
    
    @NotNull(message = "{agenciaNomeNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{agenciaNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Valid
    @JsonIgnoreProperties({"agencia", "hibernateLazyInitializer"})
    @JoinColumn(name = "endereco_id", referencedColumnName = "endereco_id", nullable = false)
    @OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    private Endereco endereco;
    
    @JsonIgnoreProperties({"numero", "cnpj", "nome", "hibernateLazyInitializer"})
    @JoinColumn(name = "banco_id", referencedColumnName = "banco_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Banco banco;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agencia", fetch = FetchType.LAZY)
    private List<Conta> contas;
    
    public Agencia() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public List<Conta> getContas() {
        return contas;
    }

    public void setContas(List<Conta> contas) {
        this.contas = contas;
    }
}
