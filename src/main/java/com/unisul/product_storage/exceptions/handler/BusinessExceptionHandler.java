package com.unisul.product_storage.exceptions.handler;

import com.unisul.product_storage.exceptions.handler.response.ApiErroResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.ServiceUnavailableException;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BusinessExceptionHandler {

    private static final Logger LOGGER_TECNICO = LoggerFactory.getLogger(BusinessExceptionHandler.class);

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Object> handleException(ServiceUnavailableException ex) {
        var apiErro = new ApiErroResponse(SERVICE_UNAVAILABLE, ex);
        apiErro.setMensagem(ex.getMessage());
        if (ex.getCause() != null) {
            apiErro.setMensagemDetalhada(ex.getCause().getMessage());
        }
        return buildResponseEntity(apiErro, ex);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
        var apiErro = new ApiErroResponse(ex.getStatus(), ex);
        apiErro.setMensagem(ex.getBusinessMessage());
        apiErro.setMensagemDetalhada(ex.getMessage());
        return buildResponseEntity(apiErro, ex);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiErroResponse apiErro, Exception ex) {
        LOGGER_TECNICO.error("Excecao sendo capturada, APIErrorCode: {}, Mensagem: {}, Excecao: ", apiErro.getCodigoErro(), apiErro.getMensagem(), ex);
        return new ResponseEntity<>(apiErro, apiErro.getStatus());
    }
}