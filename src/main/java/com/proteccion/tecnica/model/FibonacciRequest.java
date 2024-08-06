package com.proteccion.tecnica.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class FibonacciRequest {
    @NotBlank(message = "'time' cannot be empty")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$", message = "'time' format must be HH:MM:SS")
    private String time;

    @NotBlank(message = "'email' cannot be empty")
    @Email(message = "'email' format is invalid")
    private String email;

    // Getters y Setters
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
