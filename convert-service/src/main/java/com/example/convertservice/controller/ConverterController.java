package com.example.convertservice.controller;

import com.example.convertservice.service.ConvertService;
import com.example.entity.PresentationDto;
import com.example.entity.cbr.Currency;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/convert")
public class ConverterController {
    @Autowired
    private ConvertService convertService;

    @GetMapping("/currencies")
    public List<Currency> getCurrencies(){
        return convertService.getLatestCurrencies();


    }
  
    @PostMapping("/convert")
    public ResponseEntity<PresentationDto> convert(@RequestHeader("Authorization") String token,
                                                   @Valid @RequestBody PresentationDto presentationDto) {
        ResponseEntity<PresentationDto> converted = convertService.convert(presentationDto, token);
        return ResponseEntity.status(converted.getStatusCode()).body(converted.getBody());

    }
}
