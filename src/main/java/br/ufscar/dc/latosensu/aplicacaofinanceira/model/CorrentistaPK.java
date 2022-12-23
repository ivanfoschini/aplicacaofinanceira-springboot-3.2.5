package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class CorrentistaPK implements Serializable {

    @NotNull
    @Column(name = "conta_id")
    private long contaId;
    
    @NotNull
    @Column(name = "cliente_id")
    private long clienteId;

    public CorrentistaPK() {}

    public CorrentistaPK(long contaId, long clienteId) {
        this.contaId = contaId;
        this.clienteId = clienteId;
    }

    public long getContaId() {
        return contaId;
    }

    public void setContaId(long contaId) {
        this.contaId = contaId;
    }

    public long getClienteId() {
        return clienteId;
    }

    public void setClienteId(long clienteId) {
        this.clienteId = clienteId;
    }
}
