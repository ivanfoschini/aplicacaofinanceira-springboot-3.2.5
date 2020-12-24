package br.ufscar.dc.latosensu.aplicacaofinanceira.exception;

public class BadRequestException extends Exception {

    public BadRequestException(String mensagem) {
        super(mensagem);
    }
}
