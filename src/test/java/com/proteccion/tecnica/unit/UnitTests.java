package com.proteccion.tecnica.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proteccion.tecnica.config.SecurityConfigTest;
import com.proteccion.tecnica.controller.FibonacciController;
import com.proteccion.tecnica.model.FibonacciRequest;
import com.proteccion.tecnica.model.FibonacciSequence;
import com.proteccion.tecnica.repository.FibonacciRepository;
import com.proteccion.tecnica.service.EmailService;
import com.proteccion.tecnica.service.FibonacciService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(FibonacciController.class)
@Import(SecurityConfigTest.class)
public class UnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FibonacciService fibonacciService;

    private final FibonacciService fibonacciService2 = new FibonacciService();

    @MockBean
    private EmailService emailService;

    @MockBean
    private FibonacciRepository fibonacciRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testGenerateFibonacci() throws Exception {
        // Arrange
        FibonacciRequest request = new FibonacciRequest();
        request.setTime("12:34:05");
        request.setEmail("test@example.com");

        List<Integer> fibonacciSeries = List.of(47, 29, 18, 11, 7, 4, 3);
        when(fibonacciService.generateFibonacciSeries(3, 4, 5)).thenReturn(fibonacciSeries);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/fibonacci/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Fibonacci series generated and sent to email\nHora ingresada: 12:34:05\nSerie Fibonacci: [47, 29, 18, 11, 7, 4, 3]"));
    }

    @Test
    public void testGenerateFibonacciSeriesWithPositiveSeeds() {
        // Arrange
        int seedX = 1;
        int seedY = 1;
        int count = 5;

        // Act
        List<Integer> result = fibonacciService2.generateFibonacciSeries(seedX, seedY, count);

        // Assert
        List<Integer> expected = List.of(13, 8, 5, 3, 2, 1, 1);
        assertEquals(expected, result);
    }

    @Test
    public void testGenerateFibonacciSeriesWithDifferentSeeds() {
        // Arrange
        int seedX = 2;
        int seedY = 3;
        int count = 5;

        // Act
        List<Integer> result = fibonacciService2.generateFibonacciSeries(seedX, seedY, count);

        // Assert
        List<Integer> expected = List.of(34, 21, 13, 8, 5, 3, 2);
        assertEquals(expected, result);
    }

    @Test
    public void testGenerateFibonacciSeriesWithZeroCount() {
        // Arrange
        int seedX = 1;
        int seedY = 1;
        int count = 0;

        // Act
        List<Integer> result = fibonacciService2.generateFibonacciSeries(seedX, seedY, count);

        // Assert
        List<Integer> expected = List.of(1, 1);
        assertEquals(expected, result);
    }

    @Test
    public void testGenerateFibonacciSeriesWithNegativeCount() {
        // Arrange
        int seedX = 1;
        int seedY = 1;
        int count = -3;

        // Act
        List<Integer> result = fibonacciService2.generateFibonacciSeries(seedX, seedY, count);

        // Assert
        List<Integer> expected = List.of(1, 1);
        assertEquals(expected, result);
    }

    @Test
    public void testGenerateFibonacciSeriesWithSingleElement() {
        // Arrange
        int seedX = 5;
        int seedY = 10;
        int count = 1;

        // Act
        List<Integer> result = fibonacciService2.generateFibonacciSeries(seedX, seedY, count);

        // Assert
        List<Integer> expected = List.of(15, 10, 5);
        assertEquals(expected, result);
    }
}
