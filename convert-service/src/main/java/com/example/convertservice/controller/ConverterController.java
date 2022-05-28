package com.example.convertservice.controller;

import com.example.convertservice.service.ConvertService;
import com.example.entity.PresentationDto;
import com.example.entity.cbr.Currency;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token"),
    })
    @GetMapping("/currencies")
    public List<Currency> getCurrencies(){
        return convertService.getLatestCurrencies();


    }

    @PostMapping("/convert")
    public ResponseEntity<PresentationDto> convert(@RequestHeader("Authorization") String token,
                                                   @ApiParam(name = "Converted dto", value = "Dto of the convert result", required = true)
                                                   @Valid @RequestBody PresentationDto presentationDto) {
        ResponseEntity<PresentationDto> converted = convertService.convert(presentationDto, token);
        return ResponseEntity.status(converted.getStatusCode()).body(converted.getBody());

    }
}
