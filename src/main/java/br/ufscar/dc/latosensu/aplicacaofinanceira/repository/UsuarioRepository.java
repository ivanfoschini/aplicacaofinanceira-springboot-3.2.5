package br.ufscar.dc.latosensu.aplicacaofinanceira.repository;

import br.ufscar.dc.latosensu.aplicacaofinanceira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    @Query("select usuario from Usuario usuario where usuario.nomeDeUsuario = :nomeDeUsuario")
    Usuario findByNomeDeUsuario(@Param("nomeDeUsuario") String nomeDeUsuario);
    
    @Query("select usuario from Usuario usuario where usuario.nomeDeUsuario = :nomeDeUsuario and usuario.senha = :senha")
    Usuario findByNomeDeUsuarioAndSenha(@Param("nomeDeUsuario") String nomeDeUsuario, @Param("senha") String senha);
}
