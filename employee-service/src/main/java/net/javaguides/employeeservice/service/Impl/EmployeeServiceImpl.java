package net.javaguides.employeeservice.service.Impl;

import lombok.AllArgsConstructor;
import net.javaguides.employeeservice.dto.ApiResponseDto;
import net.javaguides.employeeservice.dto.DepartmentDto;
import net.javaguides.employeeservice.dto.EmployeeDto;
import net.javaguides.employeeservice.entity.Employee;
import net.javaguides.employeeservice.exception.ResourceNotFoundException;
import net.javaguides.employeeservice.repository.EmployeeRepository;
import net.javaguides.employeeservice.service.APIClient;
import net.javaguides.employeeservice.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

   // private RestTemplate restTemplate;
   // private WebClient webClient;
    private APIClient apiClient;
    private EmployeeRepository employeeRepository;
    private ModelMapper modelMapper;
    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {

       Employee employee= modelMapper.map(employeeDto,Employee.class);

       Employee savedEmployee= employeeRepository.save(employee);

       EmployeeDto savedEmployeeDto = modelMapper.map(savedEmployee,EmployeeDto.class);

        return  savedEmployeeDto;
    }
    @Override
    public ApiResponseDto getEmployeeById(Long employeeId) {

      Employee employee =  employeeRepository.findById(employeeId)
                             .orElseThrow( ()-> new ResourceNotFoundException(
                                                  "Employee",
                                                  "Employee Id",
                                                   employeeId
                                          )
      );
      /*
      ResponseEntity<DepartmentDto> departmentDtoResponseEntity =
                      restTemplate.getForEntity("http://localhost:8080/api/departments/"+employee.getDepartmentCode()
                                       , DepartmentDto.class);

      DepartmentDto departmentDto = departmentDtoResponseEntity.getBody();
      */
      /*DepartmentDto departmentDto = webClient.get()
                                .uri("http://localhost:8080/api/departments/"+employee.getDepartmentCode())
                                .retrieve()
                                .bodyToMono(DepartmentDto.class)
                                .block();
      */
      DepartmentDto departmentDto= apiClient.getDepartmentByCode(employee.getDepartmentCode());

      EmployeeDto employeeDto = modelMapper.map(employee,EmployeeDto.class);

      ApiResponseDto apiResponseDto = new ApiResponseDto(
                                                        employeeDto,
                                                        departmentDto
                                                        );

      return apiResponseDto;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {

      List<Employee> employees =  employeeRepository.findAll().stream().collect(Collectors.toList());

      return employees.stream().map((employee)-> modelMapper.map(employee,EmployeeDto.class))
                                      .collect(Collectors.toList());
    }
}
