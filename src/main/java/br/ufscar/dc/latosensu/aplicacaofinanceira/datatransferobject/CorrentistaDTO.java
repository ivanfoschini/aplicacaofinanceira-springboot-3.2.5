package br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CorrentistaDTO {

    @NotNull(message = "{correntistaContaIdNaoPodeSerNulo}")
    private Long contaId;

    @Valid
    @NotEmpty(message = "{correntistaClienteTitularidadeNaoPodeSerVazio}")
    private List<ClienteTitularidadeDTO> clientes;

    public Long getContaId() {
        return contaId;
    }

    public void setContaId(Long contaId) {
        this.contaId = contaId;
    }

    public List<ClienteTitularidadeDTO> getClientes() {
        return clientes;
    }

    public void setClientes(List<ClienteTitularidadeDTO> clientes) {
        this.clientes = clientes;
    }
}
