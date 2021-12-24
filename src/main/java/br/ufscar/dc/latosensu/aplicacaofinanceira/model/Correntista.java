package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "correntista")
public class Correntista {
    
    @JsonIgnore
    @EmbeddedId
    protected CorrentistaPK correntistaPK;
    
    @NotNull
    @Column(name = "titularidade")
    private boolean titularidade;
    
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    @JoinColumn(name = "conta_id", referencedColumnName = "conta_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Conta conta;
    
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    @JoinColumn(name = "cliente_id", referencedColumnName = "cliente_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cliente cliente;

    public Correntista() {}

    public Correntista(CorrentistaPK correntistaPK, boolean titularidade, Conta conta, Cliente cliente) {
        this.correntistaPK = correntistaPK;
        this.titularidade = titularidade;
        this.conta = conta;
        this.cliente = cliente;
    }

    public CorrentistaPK getCorrentistaPK() {
        return correntistaPK;
    }

    public void setCorrentistaPK(CorrentistaPK correntistaPK) {
        this.correntistaPK = correntistaPK;
    }

    public boolean getTitularidade() {
        return titularidade;
    }

    public void setTitularidade(boolean titularidade) {
        this.titularidade = titularidade;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}