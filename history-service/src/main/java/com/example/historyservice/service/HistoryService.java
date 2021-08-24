package com.example.historyservice.service;

import com.example.entity.PresentationDto;
import com.example.entity.cbr.Currency;
import com.example.entity.cbr.ValCurs;
import com.example.historyservice.specification.HistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HistoryService {
    private HistoryRepo historyRepo;
    @Autowired
    public HistoryService(HistoryRepo historyRepo) {
        this.historyRepo = historyRepo;
    }

//    public List<Currency> getCurrencies() {
//        if (cbrRepo.findAll().isEmpty()){
//
//            ValCurs valCurs = template.getForObject(
//                    URL, ValCurs.class);
//            List<Currency> currencies = valCurs.getCurrencies();
//            currencies.forEach(c -> {
//                c.setDate((valCurs.getDate()));
//                save(c);
//
//            });
//            save(createRub());
//        }
//
//        return cbrRepo.findAll();
//    }
        public PresentationDto saveDto(PresentationDto dto) {
        return historyRepo.save(dto);
    }
}
