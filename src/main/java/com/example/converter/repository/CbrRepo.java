package com.example.converter.repository;

import com.example.converter.entity.cbr.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CbrRepo extends JpaRepository<Currency,Integer> {
    Currency findByCharCodeAndDate(String charCode, LocalDate date);
    Currency findByCharCode(String charCode);
    Currency findFirstByOrderByDateDesc();

}
