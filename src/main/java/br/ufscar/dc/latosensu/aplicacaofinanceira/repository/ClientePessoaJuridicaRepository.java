package br.ufscar.dc.latosensu.aplicacaofinanceira.repository;

import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Cliente;
import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ClientePessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientePessoaJuridicaRepository extends JpaRepository<ClientePessoaJuridica, Long> {
    
    Cliente findById(long id);
}
