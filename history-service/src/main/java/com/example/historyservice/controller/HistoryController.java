package com.example.historyservice.controller;

import com.example.entity.HistoryPage;
import com.example.entity.PresentationDto;
import com.example.historyservice.service.HistoryService;
import com.example.historyservice.repository.specification.DtoSpec;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    //replaced with rabbitmq
    @PostMapping(value = "/save",name = "saveToHistory (used by convert-service)" )
    @ApiIgnore
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token"),
    })
    public ResponseEntity<PresentationDto> saveToHistory(
            @ApiParam(name = "Converted dto", value = "Dto to save in history db", required = true)
            @RequestBody PresentationDto presentationDto){
       return new ResponseEntity<>(historyService.saveDto(presentationDto), HttpStatus.CREATED);
    }

    @GetMapping("/show")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token"),
    })
    private ResponseEntity<HistoryPage> showHistory(
                                                    @RequestParam(required = false,defaultValue = "5") int pageSize,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date,
                                                    @RequestParam(required = false) String baseCurrency,
                                                    @RequestParam(required = false) String targetCurrency){
        return findPaginated(1,pageSize,  "date","desc",date,baseCurrency,targetCurrency);
    }
    @GetMapping("show/{pageNumber}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token"),
    })
    public ResponseEntity<HistoryPage> findPaginated(@PathVariable int pageNumber,
                                                               @RequestParam (required = false,defaultValue = "5") int pageSize,
                                                               @RequestParam (required = false,defaultValue = "date") String sortField,
                                                               @RequestParam (required = false,defaultValue = "desc") String dir,
                                                               @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date,
                                                               String baseCurrency, String targetCurrency){
        Specification<PresentationDto> spec=Specification.where(null);
        if (date!=null) {
            spec=spec.and(DtoSpec.getDate(date));
        }
        if (baseCurrency!=null) {
            spec=spec.and(DtoSpec.getBaseCurrency(baseCurrency));

        }
        if (targetCurrency!=null) {
            spec=spec.and(DtoSpec.getTargetCurrency(targetCurrency));

        }
        Page<PresentationDto> page=historyService.findPaginated(spec,pageNumber,pageSize,sortField,dir);
        return new ResponseEntity<>(new HistoryPage(page.getContent(), page.getTotalPages(), page.getTotalElements()),HttpStatus.OK);
    }

}
