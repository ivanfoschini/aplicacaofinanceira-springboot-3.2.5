package br.ufscar.dc.latosensu.aplicacaofinanceira.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "estado")
public class Estado {
    
    @Id
    @SequenceGenerator(name = "Estado_Generator", sequenceName = "estado_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Estado_Generator")
    @Column(name = "estado_id", nullable = false)
    private Long id;
    
    @NotNull(message = "{estadoNomeNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{estadoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    @Column(name = "nome", nullable = false, length = 255, unique = true)
    private String nome;

    public Estado() {}

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
}
