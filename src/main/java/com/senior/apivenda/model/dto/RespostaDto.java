package com.senior.apivenda.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespostaDto implements Serializable {
    private String status;
    private String error;
    private String message;
    private String timestamp;

    public static RespostaDto sucess(String msgError, HttpStatus status){
        return RespostaDto.builder()
                .status(String.valueOf(status.value()))
                .timestamp(LocalDateTime.now().toString())
                .message(msgError)
                .build();
    }

    public static RespostaDto error(String msgError, HttpStatus status){
        return RespostaDto.builder()
                .status(String.valueOf(status.value()))
                .error(status.getReasonPhrase())
                .timestamp(LocalDateTime.now().toString())
                .message(msgError)
                .build();

    }
}
