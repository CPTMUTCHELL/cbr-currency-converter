package com.example.converter.controller;

import com.example.converter.entity.PresentationDto;
import com.example.converter.repository.specification.DtoSpec;
import com.example.converter.service.DtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    private DtoService dtoService;
    @GetMapping
    private String showHistory(Model model,
                               @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date,
                               @RequestParam(required = false) String baseCurrency,
                               @RequestParam(required = false) String targetCurrency){
        return findPaginated(1,model, "date","desc",date,baseCurrency,targetCurrency);
    }
    @GetMapping("/{pageNumber}")
    public String findPaginated(@PathVariable int pageNumber, Model model,
                              @RequestParam (required = false,defaultValue = "date") String sortField,
                              @RequestParam (required = false,defaultValue = "desc") String dir,
                                LocalDate date,
                              String baseCurrency,String targetCurrency){
        Specification<PresentationDto> spec=Specification.where(null);
        int pageSize=4;
        if (date!=null) {
            spec=spec.and(DtoSpec.getDate(date));
        }
        if (baseCurrency!=null) {
            spec=spec.and(DtoSpec.getBaseCurrency(baseCurrency));

        }
        if (targetCurrency!=null) {
            spec=spec.and(DtoSpec.getTargetCurrency(targetCurrency));

        }

        model.addAttribute("currenciesList",dtoService.getCurrencies());
        Page<PresentationDto> page=dtoService.findPaginated(spec,pageNumber,pageSize,sortField,dir);
        model.addAttribute("baseCurrency",baseCurrency);
        model.addAttribute("targetCurrency",targetCurrency);
        List<PresentationDto> content = page.getContent();
        model.addAttribute("dtoList",content);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("currentPage",pageNumber);
        model.addAttribute("totalProducts",page.getTotalElements());
        model.addAttribute("sortField",sortField);
        model.addAttribute("date",date);
        model.addAttribute("dir",dir);
        model.addAttribute("reverseDir", dir.equals("asc")?"desc":"asc");
        return "historyPage";
    }


}
