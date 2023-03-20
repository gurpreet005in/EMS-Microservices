package net.javaguides.employeeservice.controller;

import lombok.AllArgsConstructor;
import net.javaguides.employeeservice.dto.ApiResponseDto;
import net.javaguides.employeeservice.dto.EmployeeDto;
import net.javaguides.employeeservice.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
public class EmployeeController {

     private EmployeeService employeeService;

     @PostMapping
     public ResponseEntity<EmployeeDto> createEmployee (@RequestBody EmployeeDto employeeDto){

         EmployeeDto saveEmployeeDto = employeeService.saveEmployee(employeeDto);
         return new ResponseEntity<>(saveEmployeeDto, HttpStatus.CREATED);
     }

     @GetMapping("/{id}")
     public ResponseEntity<ApiResponseDto> getEmployeeById (@PathVariable("id") Long employeeId){
         ApiResponseDto apiResponseDto = employeeService.getEmployeeById(employeeId);

         return new ResponseEntity<>(apiResponseDto,HttpStatus.OK);
     }

     @GetMapping
     public ResponseEntity<List<EmployeeDto>> getAllEmployees(){

           List<EmployeeDto> employeeDtoList  = employeeService.getAllEmployees();

         return new ResponseEntity<>(employeeDtoList,HttpStatus.OK);
     }
}
