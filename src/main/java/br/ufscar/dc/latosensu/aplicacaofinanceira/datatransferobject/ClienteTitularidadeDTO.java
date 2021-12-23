package br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject;

import javax.validation.constraints.NotNull;

public class ClienteTitularidadeDTO {

    @NotNull(message = "{correntistaClienteIdNaoPodeSerNulo}")
    private Long clienteId;

    @NotNull(message = "{correntistaTitularidadeNaoPodeSerNula}")
    private boolean titularidade;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public boolean isTitularidade() {
        return titularidade;
    }

    public void setTitularidade(boolean titularidade) {
        this.titularidade = titularidade;
    }
}
