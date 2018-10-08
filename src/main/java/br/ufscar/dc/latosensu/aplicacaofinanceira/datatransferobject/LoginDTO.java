package br.ufscar.dc.latosensu.aplicacaofinanceira.datatransferobject;

public class LoginDTO {
    
    private String nomeDeUsuario;
    private String senha;

    public LoginDTO() {}

    public String getNomeDeUsuario() {
        return nomeDeUsuario;
    }

    public void setNomeDeUsuario(String nomeDeUsuario) {
        this.nomeDeUsuario = nomeDeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }     
}
