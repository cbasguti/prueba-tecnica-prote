package com.proteccion.tecnica.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proteccion.tecnica.controller.FibonacciController;
import com.proteccion.tecnica.model.FibonacciRequest;
import com.proteccion.tecnica.model.FibonacciSequence;
import com.proteccion.tecnica.repository.FibonacciRepository;
import com.proteccion.tecnica.service.EmailService;
import com.proteccion.tecnica.service.FibonacciService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(FibonacciController.class)
public class UnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FibonacciService fibonacciService;

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
        request.setTime("12:34:56");
        request.setEmail("test@example.com");

        List<Integer> fibonacciSeries = List.of(1, 1, 2, 3, 5);
        when(fibonacciService.generateFibonacciSeries(3, 4, 5)).thenReturn(fibonacciSeries);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/fibonacci/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Fibonacci series generated and sent to email\nHora ingresada: 12:34:56\nSerie Fibonacci: [1, 1, 2, 3, 5]"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllSequences() throws Exception {
        // Arrange
        FibonacciSequence sequence = new FibonacciSequence(LocalTime.now(), "[1, 1, 2, 3, 5]");
        when(fibonacciRepository.findAll()).thenReturn(List.of(sequence));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/fibonacci/sequences")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sequence").value("[1, 1, 2, 3, 5]"));
    }
}
