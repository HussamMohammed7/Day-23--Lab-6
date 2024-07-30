package com.example.employeemanagement.Controller;

import com.example.employeemanagement.Api.ApiResponse;
import com.example.employeemanagement.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")

public class ControllerEmployee {








    private ArrayList<Employee> employees = new ArrayList<>();

    @PostMapping("/add")
    public ResponseEntity addEmployee(@Valid @RequestBody Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        for (Employee e : employees) {
            if (e.getID().equals(employee.getID())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse("Employee ID already exists")
                );
            }
        }

        employees.add(employee);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse("Employee added successfully")
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateEmployee(@PathVariable String id, @Valid @RequestBody Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }

        int index = -1;

        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getID().equals(id)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse("Employee not found")
            );
        }
        for (Employee e : employees) {
            if (!e.getID().equals(id) && e.getID().equals(employee.getID())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse("Employee ID already exists")
                );
            }
        }

        employees.set(index, employee);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse("Employee updated")
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteEmployee(@PathVariable String id) {
        Employee employeeToRemove = null;

        for (Employee employee : employees) {
            if (employee.getID().equals(id)) {
                employeeToRemove = employee;
                break;
            }
        }

        if (employeeToRemove != null) {
            employees.remove(employeeToRemove);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse("Employee deleted")
            );
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse("Employee ID not found")
        );
    }

    @GetMapping("/get")
    public ResponseEntity getEmployees() {

        if (employees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse("Employee is empty")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity searchEmployeeById(@PathVariable String id) {
        for (Employee employee : employees) {
            if (employee.getID().equals(id)) {
                return ResponseEntity.status(HttpStatus.OK).body(employee);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse("Employee not found")
        );
    }
    @GetMapping("/age/range/")
    public ResponseEntity getAgeRange() {

        ArrayList<Employee> employeesRangeAge = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getAge()>25 && employee.getAge()<40) {
                employeesRangeAge.add(employee);
            }
        }
        if (employeesRangeAge.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse("range between 26 and 40 is empty")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
          employeesRangeAge
        );

    }
    @GetMapping("/position/{position}")
    public ResponseEntity getEmployeeByPosition(@PathVariable String position) {

        ArrayList employeesByPosition = new ArrayList<>();

        if (!position.equalsIgnoreCase("supervisor")&&
        !position.equalsIgnoreCase("coordinator")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse("postition must be 'supervisor', 'coordinator' ")
            );


        }
        for (Employee employee : employees) {
            if (employee.getPosition().equals(position)) {
                employeesByPosition.add(employee);
            }
        }
        if (employeesByPosition.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse("Employee is empty")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                employeesByPosition
        );



    }
    @GetMapping("/annual-leave/{id}")
    public ResponseEntity annualLeaveApply(@PathVariable String id) {

        int index = -1;

        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getID().equals(id)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse("Employee doesnt exists")
            );
        }

        if (employees.get(index).isOnLeave()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse("Employee is on-leave")
            );
        }
        if (employees.get(index).getAnnualLeave() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse("Employee doesnt have annual leaves")
            );
        }

        employees.get(index).setOnLeave(true);
        employees.get(index).setAnnualLeave(employees.get(index).getAnnualLeave() - 1);
        return ResponseEntity.status(HttpStatus.OK).body(

                new ApiResponse("Employee annual leave successfully")
        );
    }
        @GetMapping("/annual-leave/get")
        public ResponseEntity employeeNoAnnualLeave(){

            ArrayList <Employee> employeesNoAnnualLeave = new ArrayList<>();
            for (Employee employee : employees) {
                if (employee.getAnnualLeave()<=0) {
                    employeesNoAnnualLeave.add(employee);
                }
            }
            if (employeesNoAnnualLeave.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse("employeesNoAnnualLeave is empty")
                );
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    employeesNoAnnualLeave
            );


        }
        @PostMapping("/Promote")
        public ResponseEntity promote(@RequestParam String idRequest , @RequestParam String idEmployee ){

            int indexRequester = -1;
            int indexEmployee = -1;

            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getID().equals(idRequest)) {
                    indexRequester = i;
                    break;
                }
            }

            if (indexRequester == -1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse("EmployeeRequester is doesnt exists")
                );
            }

            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getID().equals(idEmployee)) {
                    indexEmployee = i;
                    break;
                }
            }
            if (indexEmployee == -1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse("idEmployee doesnt exists")
                );
            }

            if(!employees.get(indexRequester).getPosition().equalsIgnoreCase("supervisor")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse("Employee Requester must be supervisor")
                );
            }
            if(employees.get(indexEmployee).getPosition().equalsIgnoreCase("supervisor")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse("Employee already supervisor")
                );
            }
            if(employees.get(indexEmployee).getAge() <30){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse("Employee must be 30 years old or older ")
                );

            }
            if(employees.get(indexEmployee).isOnLeave()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponse("Cant not promote ,Employee is on-leave")
                );
            }

            employees.get(indexEmployee).setPosition("supervisor");
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponse("Employee promoted successfully")
            );





        }


    }










