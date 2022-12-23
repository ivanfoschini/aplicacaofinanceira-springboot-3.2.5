package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

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
