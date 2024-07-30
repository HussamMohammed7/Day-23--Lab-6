package com.example.employeemanagement.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;


@Data
@AllArgsConstructor

public class Employee {



    @NotEmpty(message = "ID should be not empty")
    @Size(min = 3,message = "ID must more then 2")
    private String ID ;

    @NotEmpty(message = "name cant be empty")
    @Size(min = 5 ,message = "name Length more than 4 ")
    private String name ;

    @NotEmpty(message = "Email should be not empty")
    @Email
    private String email;


    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with '05' and contain exactly 10 digits.")
    private String phone_number;



    @NotNull(message = "age cant be null")
    @Positive(message = "age should be a number ")
    @Min(value = 26,message = "age must be more than 25 ")
    private int age ;


    @NotEmpty(message = "position cant be null")
    @Pattern(regexp = "supervisor|coordinator",
            message = "position must be 'supervisor', 'coordinator'")
    private String position ;

    @AssertFalse(message = "The onLeave field must be initially set to false.")
    private boolean onLeave;

    @NotNull(message = "Hire date cannot be null.")
    @PastOrPresent(message = "Hire date must be in the past or present.")

    private LocalDate hireDate ;

    @NotNull(message = "annualLeave cant be null")
    @Positive(message = "annualLeave should be a number ")
    private double annualLeave;







}
