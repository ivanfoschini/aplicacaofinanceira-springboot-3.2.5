package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "conta")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo", length = 1)
public abstract class Conta {
    
    @Id
    @SequenceGenerator(name="Conta_Generator", sequenceName="conta_sequence", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="Conta_Generator")            
    @Column(name = "conta_id", nullable = false)
    private Long id;
    
    @NotNull(message = "{contaNumeroNaoPodeSerNulo}")
    @Min(value = 1, message = "{contaNumeroDeveSerMaiorDoQueZero}")
    @Column(name = "numero", nullable = false, unique = true)
    private int numero;
    
    @NotNull(message = "{contaSaldoNaoPodeSerNulo}")
    @Column(name = "saldo", nullable = false)
    private float saldo;
    
    @NotNull(message = "{contaDataDeAberturaNaoPodeSerNula}")
    @Column(name = "data_de_abertura", nullable = false)
    private Date dataDeAbertura;
        
    @JsonIgnoreProperties({"numero", "nome", "endereco", "banco", "hibernateLazyInitializer"})
    @JoinColumn(name = "agencia_id", referencedColumnName = "agencia_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Agencia agencia;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conta", fetch = FetchType.LAZY)
    private List<Correntista> correntistas;
        
    protected Conta() {}

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

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public Date getDataDeAbertura() {
        return dataDeAbertura;
    }

    public void setDataDeAbertura(Date dataDeAbertura) {
        this.dataDeAbertura = dataDeAbertura;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }

    public List<Correntista> getCorrentistas() {
        return correntistas;
    }

    public void setCorrentistas(List<Correntista> correntistas) {
        this.correntistas = correntistas;
    }
}
