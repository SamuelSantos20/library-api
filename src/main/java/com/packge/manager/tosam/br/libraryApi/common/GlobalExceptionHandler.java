package com.packge.manager.tosam.br.libraryApi.common;

import com.packge.manager.tosam.br.libraryApi.dto.ErroCampo;
import com.packge.manager.tosam.br.libraryApi.dto.ErroRespoosta;
import com.packge.manager.tosam.br.libraryApi.exceptions.OperacaoNaoPermitidaException;
import com.packge.manager.tosam.br.libraryApi.exceptions.RegistroDuplicadoExeption;
import com.packge.manager.tosam.br.libraryApi.exceptions.RegradeNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


 
@RestControllerAdvice
public class GlobalExceptionHandler {

     
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroRespoosta handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        List<FieldError> fieldErrors = e.getFieldErrors();

        List<ErroCampo> collectErroCampos =
                fieldErrors.stream()
                        .map(fieldError -> new ErroCampo(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()))
                        .collect(Collectors.toList());

        return new ErroRespoosta(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro validação", collectErroCampos);


    }

    @ExceptionHandler(RegistroDuplicadoExeption.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroRespoosta hedleRegistroDuplicadoExeption(RegistroDuplicadoExeption e) {

        return ErroRespoosta.conflito(e.getMessage());


    }


    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroRespoosta handleOperacaoNaoPermitidaException(OperacaoNaoPermitidaException e) {


        return ErroRespoosta.respostaPadrao(e.getMessage());

    }


    @ExceptionHandler(RegradeNegocioException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroRespoosta handlerRegradeNegocioException(RegradeNegocioException e) {

        return new ErroRespoosta(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro validação", List.of(new ErroCampo(e.getCampo(), e.getMessage())));

    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErroRespoosta  handlerAccessDeniedException(AccessDeniedException e) {

        return new ErroRespoosta(HttpStatus.FORBIDDEN.value(), "Acesso Negado!" , List.of());
    }




    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroRespoosta handleErrosNaoTratados(RuntimeException e) {

        return new ErroRespoosta(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ocorreu um erro inesperado entre em contato com a admnistração.", List.of());

    }



}
