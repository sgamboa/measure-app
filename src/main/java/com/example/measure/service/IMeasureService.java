package com.example.measure.service;

import com.example.measure.entity.Measure;
import java.util.List;

public interface IMeasureService {
    
    List<Measure> getAllMeasures();

    Measure saveNewMeasure(Measure measure);

}
