package com.example.measure.repository;

import com.example.measure.entity.Measure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMeasureRepository extends JpaRepository<Measure, Long> {
    
}
