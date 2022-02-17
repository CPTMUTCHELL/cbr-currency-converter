package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class HistoryPage {

    private final List<PresentationDto> dto;
    private final int totalPages;
    private final long totalElements;

}
