package com.unisul.product_storage.exceptions.handler;

import com.unisul.product_storage.exceptions.handler.response.ApiErroResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER_TECNICO = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        String erro = ex.getParameterName() + " parametro nao informado";
        return buildResponseEntity(new ApiErroResponse(BAD_REQUEST, erro, ex), ex);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        var apiErro = new ApiErroResponse(BAD_REQUEST);
        apiErro.setMensagem("Erro de validação");

        apiErro.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiErro.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiErro, ex);
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            jakarta.validation.ConstraintViolationException ex) {
        var apiErro = new ApiErroResponse(BAD_REQUEST);
        apiErro.setMensagem("Erro de validação");
        return buildResponseEntity(apiErro, ex);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var servletWebRequest = (ServletWebRequest) request;
        LOGGER_TECNICO.debug("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        var error = "Malformed JSON request";
        return buildResponseEntity(new ApiErroResponse(HttpStatus.BAD_REQUEST, error, ex), ex);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            HttpMessageNotWritableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        var error = "Erro ao realizar escrita de saída JSON";
        return buildResponseEntity(new ApiErroResponse(INTERNAL_SERVER_ERROR, error, ex), ex);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        var apiErro = new ApiErroResponse(BAD_REQUEST);
        apiErro.setMensagem(String.format("Nao pode encontrar o %s metodo para a URL %s",
                ex.getHttpMethod(), ex.getRequestURL()));
        apiErro.setMensagemDetalhada(ex.getMessage());
        return buildResponseEntity(apiErro, ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        var apiErro = new ApiErroResponse(BAD_REQUEST);
        apiErro.setMensagem(String.format("O parametro '%s' do valor '%s' nao pode ser convertido para o tipo '%s'",
                ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName()));
        apiErro.setMensagemDetalhada(ex.getMessage());
        return buildResponseEntity(apiErro, ex);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<Object> handleConversionFailedException(ConversionFailedException ex) {
        var apiErro = new ApiErroResponse(BAD_REQUEST, ex);
        return buildResponseEntity(apiErro, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        var apiErro = new ApiErroResponse(INTERNAL_SERVER_ERROR, ex);
        return buildResponseEntity(apiErro, ex);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErroResponse apiErro, Exception ex) {
        LOGGER_TECNICO.error("Exceção sendo capturada pelo RestExceptionHandler, APIErrorCode: {}, Mensagem: {}, Excecao: ", apiErro.getCodigoErro(), apiErro.getMensagem(), ex);
        return new ResponseEntity<>(apiErro, apiErro.getStatus());
    }
}