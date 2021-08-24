package com.example.convertservice.controller;

import com.example.convertservice.service.ConvertService;
import com.example.entity.PresentationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/converter")
public class ConverterController {
    @Autowired
    private ConvertService convertService;

//    @GetMapping()
//    public String convert(Model model){
//        List<Currency> names=convertService.getCurrencies();
//        PresentationDto presentationDto=new PresentationDto();
//        model.addAttribute("currencies",names);
//        model.addAttribute("presentation",presentationDto);
//
//
//        return "converterPage";
//    }
    @PostMapping("/convert")
    public ResponseEntity<PresentationDto> convert(@RequestBody PresentationDto presentationDto
                                  ){
        PresentationDto converted = convertService.convert(presentationDto);

        return new ResponseEntity<>(converted, HttpStatus.CREATED);

    }

}
