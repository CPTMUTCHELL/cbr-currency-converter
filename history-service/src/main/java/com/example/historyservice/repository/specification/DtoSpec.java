package com.example.historyservice.repository.specification;

import com.example.entity.PresentationDto;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class DtoSpec {
    public static Specification<PresentationDto> getDate(LocalDate date){
        return (Specification<PresentationDto>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("date"),date);
    }

    public static Specification<PresentationDto> getBaseCurrency(String baseCurrency){
        return (Specification<PresentationDto>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("baseCurrency"),"%"+baseCurrency+"%");
    }
    public static Specification<PresentationDto> getTargetCurrency(String targetCurrency){
        return (Specification<PresentationDto>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("targetCurrency"),"%"+ targetCurrency +"%");
    }

}