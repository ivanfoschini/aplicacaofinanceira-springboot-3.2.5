package br.ufscar.dc.latosensu.aplicacaofinanceira.exception;

public class MoreThanOneAccountClientException extends Exception {
    
    public MoreThanOneAccountClientException(String mensagem) {
        super(mensagem);
    }    
}
