package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(notes = "List of converts")
    private final List<PresentationDto> dto;
    @ApiModelProperty(notes = "Max amount of pages")
    private final int totalPages;
    @ApiModelProperty(notes = "Max amount of converts")
    private final long totalElements;

}
