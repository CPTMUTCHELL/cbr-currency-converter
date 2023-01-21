package com.example.convertservice.repository;

import com.example.entity.cbr.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CbrRepo extends JpaRepository<Currency,Integer> {
    Currency findByCharCodeAndDate(String charCode, LocalDate date);
    Currency findByCharCode(String charCode);
    Currency findFirstByOrderByDateDesc();
    List<Currency> findTop35ByOrderByDateDesc();
}