package com.proteccion.tecnica.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FibonacciService {

    public List<Integer> generateFibonacciSeries(int seedX, int seedY, int count) {
        List<Integer> series = new ArrayList<>();

        if (count <= 0) {
            series.add(seedX);
            series.add(seedY);
            series.sort(Collections.reverseOrder());
            return series;
        }

        series.add(seedX);
        series.add(seedY);

        for (int i = 2; i < count + 2; i++) {
            int next = series.get(i - 1) + series.get(i - 2);
            series.add(next);
        }

        series.sort(Collections.reverseOrder());

        return series;
    }
}
