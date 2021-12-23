package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import br.ufscar.dc.latosensu.aplicacaofinanceira.validation.ClienteStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.CascadeType;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "cliente")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", length = 1)
public abstract class Cliente {

    @Id
    @Column(name = "cliente_id", nullable = false)
    @SequenceGenerator(name="Cliente_Generator", sequenceName="cliente_sequence", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="Cliente_Generator")
    private Long id;

    @NotNull(message = "{clienteNomeNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{clienteNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @NotNull(message = "{clienteStatusNaoPodeSerNulo}")
    @ClienteStatus(message = "{clienteStatusInvalido}")
    @Column(name = "status", nullable = false, length = 7)
    private String status;

    @Valid
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Endereco> enderecos;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Correntista> correntistas;

    protected Cliente() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public List<Correntista> getCorrentistas() {
        return correntistas;
    }

    public void setCorrentistas(List<Correntista> correntistas) {
        this.correntistas = correntistas;
    }
}
