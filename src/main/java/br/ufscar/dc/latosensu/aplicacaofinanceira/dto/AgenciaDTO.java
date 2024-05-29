package br.ufscar.dc.latosensu.aplicacaofinanceira.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AgenciaDTO {

    @NotNull(message = "{agenciaNumeroNaoPodeSerNulo}")
    @Min(value = 1, message = "{agenciaNumeroDeveSerMaiorDoQueZero}")
    private int numero;

    @NotNull(message = "{agenciaNomeNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{agenciaNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    private String nome;

    private EnderecoDTO endereco;

    private Long bancoId;

    public AgenciaDTO() {}

    public int getNumero() {
        return numero;
    }

    public String getNome() {
        return nome;
    }

    public EnderecoDTO getEndereco() {
        return endereco;
    }

    public Long getBancoId() {
        return bancoId;
    }
}
