package br.ufscar.dc.latosensu.aplicacaofinanceira.dto;

import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Estado;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CidadeDTO {

    @NotNull(message = "{cidadeNomeNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{cidadeNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    private String nome;

    private Estado estado;

    public CidadeDTO() {}

    public String getNome() {
        return nome;
    }

    public Estado getEstado() {
        return estado;
    }
}
