package br.ufscar.dc.latosensu.aplicacaofinanceira.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CnpjValidator.class)
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cnpj {
    public static final String DEFAULT_MESSAGE = "O CNPJ fornecido e invalido";
    
    String message() default DEFAULT_MESSAGE;
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default {};
}
