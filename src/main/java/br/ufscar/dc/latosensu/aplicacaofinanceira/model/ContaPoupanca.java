package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "conta_poupanca")
@DiscriminatorValue(value = "P")
public class ContaPoupanca extends Conta implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "{contaPoupancaDataDeAniversarioNaoPodeSerNula}")
    @Column(name = "data_de_aniversario", nullable = false)
    private Date dataDeAniversario;
    
    @NotNull(message = "{contaPoupancaCorrecaoMonetariaNaoPodeSerNula}")
    @Min(value = 0, message = "{contaPoupancaCorrecaoMonetariaDeveSerMaiorOuIgualAZero}")
    @Column(name = "correcao_monetaria", nullable = false)
    private float correcaoMonetaria;
    
    @NotNull(message = "{contaPoupancaJurosNaoPodeSerNulo}")
    @Min(value = 0, message = "{contaPoupancaJurosDeveSerMaiorOuIgualAZero}")
    @Column(name = "juros", nullable = false)
    private float juros;

    public ContaPoupanca() {}

    public Date getDataDeAniversario() {
        return dataDeAniversario;
    }

    public void setDataDeAniversario(Date dataDeAniversario) {
        this.dataDeAniversario = dataDeAniversario;
    }

    public float getCorrecaoMonetaria() {
        return correcaoMonetaria;
    }

    public void setCorrecaoMonetaria(float correcaoMonetaria) {
        this.correcaoMonetaria = correcaoMonetaria;
    }

    public float getJuros() {
        return juros;
    }

    public void setJuros(float juros) {
        this.juros = juros;
    }
}
