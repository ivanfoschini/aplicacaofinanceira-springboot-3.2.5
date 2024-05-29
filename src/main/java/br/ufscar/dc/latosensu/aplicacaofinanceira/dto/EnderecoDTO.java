package br.ufscar.dc.latosensu.aplicacaofinanceira.dto;

import br.ufscar.dc.latosensu.aplicacaofinanceira.validation.Cep;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EnderecoDTO {

    @NotNull(message = "{enderecoLogradouroNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{enderecoLogradouroDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    private String logradouro;

    @NotNull(message = "{enderecoNumeroNaoPodeSerNulo}")
    @Min(value = 1, message = "{enderecoNumeroDeveSerMaiorDoQueZero}")
    private int numero;

    @Size(max = 255, message = "{enderecoComplementoDeveTerNoMaximoDuzentosECinquentaECincoCaracteres}")
    private String complemento;

    @NotNull(message = "{enderecoBairroNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{enderecoBairroDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    private String bairro;

    @NotNull(message = "{enderecoCepNaoPodeSerNulo}")
    @Cep(message = "{enderecoCepInvalido}")
    private String cep;

    private Long cidadeId;

    public EnderecoDTO() {}

    public String getLogradouro() {
        return logradouro;
    }

    public int getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCep() {
        return cep;
    }

    public Long getCidadeId() {
        return cidadeId;
    }
}
