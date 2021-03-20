package com.example.converter.controller;

import com.example.converter.entity.PresentationDto;
import com.example.converter.entity.cbr.Currency;
import com.example.converter.service.DtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/converter")
public class ConverterController {
    @Autowired
    private DtoService dtoService;

    @GetMapping()
    public String convert(Model model){
        List<Currency> names=dtoService.getCurrencies();
        PresentationDto presentationDto=new PresentationDto();
        model.addAttribute("currencies",names);
        model.addAttribute("presentation",presentationDto);


        return "converterPage";
    }
    @PostMapping("/convert")
    public String convert(@ModelAttribute(name ="presentation")PresentationDto presentationDto,
                          Model model){
        model.addAttribute("currencies",dtoService.getCurrencies());
        dtoService.convert(presentationDto);

        return "converterPage";

    }

}
