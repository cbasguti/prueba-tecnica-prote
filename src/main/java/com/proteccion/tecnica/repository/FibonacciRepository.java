package com.proteccion.tecnica.repository;

import com.proteccion.tecnica.model.FibonacciSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FibonacciRepository extends JpaRepository<FibonacciSequence, Long>{
}
