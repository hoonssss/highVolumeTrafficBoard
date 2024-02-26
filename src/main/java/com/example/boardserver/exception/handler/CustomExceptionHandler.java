package com.example.boardserver.exception.handler;

import com.example.boardserver.dto.response.CommonResponse;
import com.example.boardserver.exception.BoardServerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handlerRuntimeException(RuntimeException e){
        CommonResponse commonResponse = new CommonResponse(HttpStatus.BAD_REQUEST,"RunTimeException",e.getMessage(),e.getMessage());
        return new ResponseEntity<>(commonResponse, new HttpHeaders(), commonResponse.getStatus());
    }

    @ExceptionHandler({BoardServerException.class})
    public ResponseEntity<Object> handlerBoardServerException(BoardServerException e){
        CommonResponse commonResponse = new CommonResponse(HttpStatus.BAD_REQUEST,"BoardServerException",e.getMessage(),e.getMessage());
        return new ResponseEntity<>(commonResponse, new HttpHeaders(), commonResponse.getStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handlerException(Exception e){
        CommonResponse commonResponse = new CommonResponse(HttpStatus.BAD_REQUEST,"BoardServerException",e.getMessage(),e.getMessage());
        return new ResponseEntity<>(commonResponse, new HttpHeaders(), commonResponse.getStatus());
    }

}
