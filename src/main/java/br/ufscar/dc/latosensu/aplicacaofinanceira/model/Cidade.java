package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cidade")
public class Cidade {

    @Id
    @SequenceGenerator(name = "Cidade_Generator", sequenceName = "cidade_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Cidade_Generator")
    @Column(name = "cidade_id", nullable = false)
    private Long id;

    @NotNull(message = "{cidadeNomeNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{cidadeNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @JsonIgnoreProperties({"nome", "cidades", "hibernateLazyInitializer"})
    @JoinColumn(name = "estado_id", referencedColumnName = "estado_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estado estado;

    public Cidade() {}

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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}