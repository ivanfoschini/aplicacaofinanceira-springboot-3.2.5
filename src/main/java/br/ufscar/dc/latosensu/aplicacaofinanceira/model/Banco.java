package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import br.ufscar.dc.latosensu.aplicacaofinanceira.validation.Cnpj;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "banco")
public class Banco {

    @Id
    @SequenceGenerator(name = "Banco_Generator", sequenceName = "banco_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Banco_Generator")
    @Column(name = "banco_id", nullable = false)
    private Long id;

    @NotNull(message = "{bancoNumeroNaoPodeSerNulo}")
    @Min(value = 1, message = "{bancoNumeroDeveSerMaiorDoQueZero}")
    @Column(name = "numero", nullable = false, unique = true)
    private int numero;

    @NotNull(message = "{bancoCnpjNaoPodeSerNulo}")
    @Cnpj(message = "{bancoCnpjInvalido}")
    @Column(name = "cnpj", nullable = false, length = 14)
    private String cnpj;

    @NotNull(message = "{bancoNomeNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{bancoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "banco", fetch = FetchType.LAZY)
    private List<Agencia> agencias;

    public Banco() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Agencia> getAgencias() {
        return agencias;
    }

    public void setAgencias(List<Agencia> agencias) {
        this.agencias = agencias;
    }
}
