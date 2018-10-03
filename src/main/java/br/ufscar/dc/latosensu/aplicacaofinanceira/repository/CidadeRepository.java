package br.ufscar.dc.latosensu.aplicacaofinanceira.repository;

import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {
    
    Cidade findById(long id);
    
    @Query("select cidade from Cidade cidade left join fetch cidade.estado estado where cidade.nome = :nomeDaCidade and estado.id = :idDoEstado")
    Cidade findByNomeAndEstado(@Param("nomeDaCidade") String nomeDaCidade, @Param("idDoEstado") Long idDoEstado);

    @Query("select cidade from Cidade cidade left join fetch cidade.estado estado where cidade.nome = :nomeDaCidade and estado.id = :idDoEstadoToUpdate and cidade.id <> :idDaCidadeCurrent")
    Cidade findByNomeAndEstadoAndDifferentId(@Param("nomeDaCidade") String nomeDaCidade, @Param("idDoEstadoToUpdate") Long idDoEstadoToUpdate, @Param("idDaCidadeCurrent") Long idDaCidadeCurrent);   
}
