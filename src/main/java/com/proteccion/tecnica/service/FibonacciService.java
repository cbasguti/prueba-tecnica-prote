package com.proteccion.tecnica.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FibonacciService {

    public List<Integer> generateFibonacciSeries(int seedX, int seedY, int count){
        List<Integer> series = new ArrayList<>();
        series.add(seedX);
        series.add(seedY);

        for (int i = 2; i < count + 2; i++){
            int next = series.get(i - 1) + series.get(i - 2);
            series.add(next);
        }

        return series.subList(2, series.size());
    }
}
