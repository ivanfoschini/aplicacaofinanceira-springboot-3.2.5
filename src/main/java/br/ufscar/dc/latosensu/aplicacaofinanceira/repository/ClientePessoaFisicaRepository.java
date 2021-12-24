package br.ufscar.dc.latosensu.aplicacaofinanceira.repository;

import br.ufscar.dc.latosensu.aplicacaofinanceira.model.ClientePessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientePessoaFisicaRepository extends JpaRepository<ClientePessoaFisica, Long> {

}
