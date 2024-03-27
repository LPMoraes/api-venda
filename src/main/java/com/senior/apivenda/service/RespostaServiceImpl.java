package com.senior.apivenda.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class RespostaServiceImpl implements RespostaService{
//    @Override
//    public Map<String, String> error(String msgError, HttpStatus status){
//        Map<String, String> map =  new HashMap<>();
//        map.put("status", String.valueOf(status.value()));
//        map.put("error", status.getReasonPhrase());
//        map.put("timestamp", LocalDateTime.now().toString());
//        map.put("errorMessage", msgError);
//        return map;
//    }
}
