package br.ufscar.dc.latosensu.aplicacaofinanceira.exception;

public class MoreThanOneAccountOwnershipException extends Exception {
    
    public MoreThanOneAccountOwnershipException(String mensagem) {
        super(mensagem);
    }    
}
