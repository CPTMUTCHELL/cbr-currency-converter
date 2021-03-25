package com.example.converter.service;

import com.example.converter.entity.PresentationDto;
import com.example.converter.entity.cbr.Currency;
import com.example.converter.entity.cbr.ValCurs;
import com.example.converter.repository.CbrRepo;
import com.example.converter.repository.DtoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;

@Service
public class DtoService {

    private CbrRepo cbrRepo;
    private DtoRepo dtoRepo;
    private RestTemplate template=new RestTemplate();
    @Value(("${cbr.url}"))
    private String URL;

    @Autowired
    public DtoService(CbrRepo cbrRepo, DtoRepo dtoRepo) {
        this.cbrRepo = cbrRepo;
        this.dtoRepo = dtoRepo;

    }

    private void saveDto(PresentationDto dto) {
        dtoRepo.save(dto);
    }
    private LocalDate getLatestDateFromDb(){
       return cbrRepo.findFirstByOrderByDateDesc().getDate();
    }

    public BigDecimal convert(PresentationDto dto) {

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
        saveDto(dto);
        return res;
    }

    public List<Currency> getCurrencies() {
        if (cbrRepo.findAll().isEmpty()){

            ValCurs valCurs = template.getForObject(
                     URL, ValCurs.class);
            List<Currency> currencies = valCurs.getCurrencies();
            currencies.forEach(c -> {
                c.setDate((valCurs.getDate()));
                save(c);

            });
            save(createRub());
        }

        return cbrRepo.findAll();
    }

    private void save(Currency currency) {
        cbrRepo.save(currency);
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
                .divide(BigDecimal.valueOf(target.getNominal())), 3);
    }

    private BigDecimal convertFromCurrencyToRub(BigDecimal quantity, Currency base) {
        return quantity.multiply(base.getValue())
                .divide(BigDecimal.valueOf(base.getNominal()), 3);
    }
    public Page<PresentationDto> findPaginated(Specification<PresentationDto> spec,
                                               int pageNumber, int pageSize,
                                               String sortField, String direction){
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending():
                Sort.by(sortField).descending();
        Pageable pageable= PageRequest.of(pageNumber-1,pageSize,sort);
        return dtoRepo.findAll(spec,pageable);
    }



}
