package com.example.historyservice.controller;

import com.example.entity.PresentationDto;
import com.example.historyservice.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    private HistoryService historyService;
    @PostMapping("/save")
    public ResponseEntity<PresentationDto> saveToHistory(@RequestBody PresentationDto presentationDto){
       return new ResponseEntity<>(historyService.saveDto(presentationDto), HttpStatus.CREATED);
    }
//    @GetMapping
//    private String showHistory(Model model,
//                               @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date,
//                               @RequestParam(required = false) String baseCurrency,
//                               @RequestParam(required = false) String targetCurrency){
//        return findPaginated(1,model, "date","desc",date,baseCurrency,targetCurrency);
//    }
//    @GetMapping("/{pageNumber}")
//    public String findPaginated(@PathVariable int pageNumber, Model model,
//                                @RequestParam (required = false,defaultValue = "date") String sortField,
//                                @RequestParam (required = false,defaultValue = "desc") String dir,
//                                LocalDate date,
//                                String baseCurrency,String targetCurrency){
//        Specification<PresentationDto> spec=Specification.where(null);
//        int pageSize=4;
//        if (date!=null) {
//            spec=spec.and(DtoSpec.getDate(date));
//        }
//        if (baseCurrency!=null) {
//            spec=spec.and(DtoSpec.getBaseCurrency(baseCurrency));
//
//        }
//        if (targetCurrency!=null) {
//            spec=spec.and(DtoSpec.getTargetCurrency(targetCurrency));
//
//        }
//
//        model.addAttribute("currenciesList",dtoService.getCurrencies());
//        Page<PresentationDto> page=dtoService.findPaginated(spec,pageNumber,pageSize,sortField,dir);
//        model.addAttribute("baseCurrency",baseCurrency);
//        model.addAttribute("targetCurrency",targetCurrency);
//        List<PresentationDto> content = page.getContent();
//        model.addAttribute("dtoList",content);
//        model.addAttribute("totalPages",page.getTotalPages());
//        model.addAttribute("currentPage",pageNumber);
//        model.addAttribute("totalProducts",page.getTotalElements());
//        model.addAttribute("sortField",sortField);
//        model.addAttribute("date",date);
//        model.addAttribute("dir",dir);
//        model.addAttribute("reverseDir", dir.equals("asc")?"desc":"asc");
//        return "historyPage";
//    }

}
