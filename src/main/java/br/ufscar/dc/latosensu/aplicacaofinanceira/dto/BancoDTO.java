package br.ufscar.dc.latosensu.aplicacaofinanceira.dto;

import br.ufscar.dc.latosensu.aplicacaofinanceira.validation.Cnpj;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BancoDTO {

    @NotNull(message = "{bancoNumeroNaoPodeSerNulo}")
    @Min(value = 1, message = "{bancoNumeroDeveSerMaiorDoQueZero}")
    private int numero;

    @NotNull(message = "{bancoCnpjNaoPodeSerNulo}")
    @Cnpj(message = "{bancoCnpjInvalido}")
    private String cnpj;

    @NotNull(message = "{bancoNomeNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{bancoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    private String nome;

    public BancoDTO() {}

    public int getNumero() {
        return numero;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getNome() {
        return nome;
    }
}
