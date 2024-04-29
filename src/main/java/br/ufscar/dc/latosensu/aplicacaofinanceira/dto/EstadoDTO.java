package br.ufscar.dc.latosensu.aplicacaofinanceira.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EstadoDTO {

    @NotNull(message = "{estadoNomeNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{estadoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    private String nome;

    public EstadoDTO() {}

    public String getNome() {
        return nome;
    }
}
