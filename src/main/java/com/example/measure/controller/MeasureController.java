package com.example.measure.controller;

import com.example.measure.entity.Measure;
import com.example.measure.repository.IMeasureRepository;
import com.example.measure.service.MeasureService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MeasureController {

    @Autowired
    private MeasureService measureService;

    @Autowired
    private IMeasureRepository measureRepository;

    private List<Measure> measuresList = new ArrayList<>();

    public MeasureController(MeasureService measureService, IMeasureRepository measureRepository) {
        this.measureService = measureService;
        this.measureRepository = measureRepository;
        
        this.measuresList = this.measureRepository.findAll();
    }


    @GetMapping("/")
    public String home(Model model) {
        Measure measure = new Measure();

        model.addAttribute("measure", measure);
        model.addAttribute("measureList", measuresList);

        return "create_measure";
    }    
    
    @GetMapping("/measures")
    public String measureList(Model model) {
        model.addAttribute("measures", measureService.getAllMeasures());
        return "measures";
    }
    
    @GetMapping("/measure/new")
    public String createMeasureForm(Model model){
        
        Measure measure = new Measure();
       
        model.addAttribute("measure", measure);
        model.addAttribute("measureList", measuresList);

        return "create_measure";
    }
    
    @PostMapping("/measure")
    public String saveMeasure(@ModelAttribute("measure") Measure measure) {
        measureService.saveNewMeasure(measure);
        return "redirect:/measures";
    }
}
