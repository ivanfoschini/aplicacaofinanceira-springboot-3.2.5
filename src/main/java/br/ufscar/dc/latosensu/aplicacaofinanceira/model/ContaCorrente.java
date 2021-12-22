package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "conta_corrente")
@DiscriminatorValue(value = "C")
public class ContaCorrente extends Conta {
    
    @NotNull(message = "{contaCorrenteLimiteNaoPodeSerNulo}")
    @Min(value = 0, message = "{contaCorrenteLimiteDeveSerMaiorOuIgualAZero}")
    @Column(name = "limite", nullable = false)
    private float limite;

    public ContaCorrente() {}

    public float getLimite() {
        return limite;
    }

    public void setLimite(float limite) {
        this.limite = limite;
    }    
}
