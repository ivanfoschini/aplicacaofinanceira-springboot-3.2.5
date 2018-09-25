package br.ufscar.dc.latosensu.aplicacaofinanceira.repository;

import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Long> {
    
    Banco findById(long id);
    
    @Query("select banco from Banco banco where banco.numero = :numero")
    Banco findByNumero(@Param("numero") Integer numero);
    
    @Query("select banco from Banco banco where banco.numero = :numero and banco.id <> :id")
    Banco findByNumeroAndDifferentId(@Param("numero") Integer numero, @Param("id") Long id);    
}
