package com.example.convertservice.service;

import com.example.convertservice.repository.CbrRepo;
import com.example.entity.PresentationDto;
import com.example.entity.cbr.Currency;
import com.example.entity.cbr.ValCurs;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service

public class ConvertService {
//    @Value(("${spring.rabbitmq.exchange}"))
//    private String exc;

//    @Autowired
//    private AmqpTemplate rabbitTemplate;
    private CbrRepo cbrRepo;
//    private DtoRepo dtoRepo;
    private RestTemplate template=new RestTemplate();
    @Value(("${cbr.url}"))
    private String URL;
    @Value(("${history.url}"))
    private String historyURL;
    @Autowired
    public ConvertService(CbrRepo cbrRepo) {
        this.cbrRepo = cbrRepo;

    }


    private LocalDate getLatestDateFromDb(){
        return cbrRepo.findFirstByOrderByDateDesc().getDate();
    }

    public ResponseEntity<PresentationDto> convert(PresentationDto dto,String token) {
        if (cbrRepo.findAll().isEmpty())   insertIfEmpty();
        BigDecimal res;
        Currency target;
        Currency base;
        if (!LocalDate.now().plusDays(1).isBefore(getLatestDateFromDb()) && !LocalDate.now().plusDays(1).isEqual(getLatestDateFromDb())) {
            insertNewCurrenciesWithNewDate();
        }
        target = findCurrencyByCharCodeAndDate(dto.getTargetCurrency(), getLatestDateFromDb());
        base = findCurrencyByCharCodeAndDate(dto.getBaseCurrency(), getLatestDateFromDb());

        if (dto.getBaseCurrency().equals("RUB")) {
            res = convertFromRubToCurrency(dto.getQuantityToConvert(), target);
        } else if (dto.getTargetCurrency().equals("RUB")) {
            res = convertFromCurrencyToRub(dto.getQuantityToConvert(), base);
        } else {
            res = convertFromCurrencyToRub(dto.getQuantityToConvert(), base);
            res = convertFromRubToCurrency(res, target);
        }
        dto.setResult(res);
        dto.setDate(LocalDate.now());
        dto.setBaseCurrency(dto.getBaseCurrency()+"("
                +  findCurrencyByCharCodeAndDate(dto.getBaseCurrency(),getLatestDateFromDb()).getName()+")");
        dto.setTargetCurrency(dto.getTargetCurrency()+"("
                + findCurrencyByCharCodeAndDate(dto.getTargetCurrency(), getLatestDateFromDb()).getName()+")");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<PresentationDto> entityReq = new HttpEntity<>(dto, headers);
//        rabbitTemplate.convertAndSend(exc,"history.",dto);
        ResponseEntity<PresentationDto> result = template.exchange(historyURL+"/backend/history/save", HttpMethod.POST, entityReq, PresentationDto.class);
         return result;
//        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    public List<Currency> getLatestCurrencies() {
        if (cbrRepo.findAll().isEmpty()){

         insertIfEmpty();
        }

        return cbrRepo.findTop35ByOrderByDateDesc();
    }

    private void save(Currency currency) {
        cbrRepo.save(currency);
    }
    private void insertIfEmpty(){
        ValCurs valCurs = template.getForObject(
                URL, ValCurs.class);
        List<Currency> currencies = valCurs.getCurrencies();
        currencies.forEach(c -> {
            c.setDate((valCurs.getDate()));
            save(c);

        });
        save(createRub());
    }


    private void insertNewCurrenciesWithNewDate(){
        ValCurs valCurs = template.getForObject(
                URL, ValCurs.class);
        List<Currency> currencies = valCurs.getCurrencies();
        if (!valCurs.getDate().isEqual(cbrRepo.findFirstByOrderByDateDesc().getDate())){

            currencies.forEach(c -> {
                c.setDate((valCurs.getDate()));
                save(c);
            });
            save(createRub());

        }
    }
    private Currency createRub(){
        Currency rub=new Currency();
        rub.setId("R0000");
        rub.setCharCode("RUB");
        rub.setDate(getLatestDateFromDb());
        rub.setName("Российский рубль");
        rub.setValue(BigDecimal.valueOf(1));
        rub.setNominal(1);
        rub.setNumCode(643);
        return rub;
    }
    private Currency findCurrencyByCharCodeAndDate(String charCode,LocalDate date) {
        return cbrRepo.findByCharCodeAndDate(charCode,date);
    }


    private BigDecimal convertFromRubToCurrency(BigDecimal quantity, Currency target) {
        return quantity.divide(target.getValue()
                .divide(BigDecimal.valueOf(target.getNominal())), 3, RoundingMode.CEILING);
    }

    private BigDecimal convertFromCurrencyToRub(BigDecimal quantity, Currency base) {
        return quantity.multiply(base.getValue())
                .divide(BigDecimal.valueOf(base.getNominal()), 3,RoundingMode.CEILING);
    }


}
