package net.javaguides.employeeservice.service.Impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import net.javaguides.employeeservice.dto.ApiResponseDto;
import net.javaguides.employeeservice.dto.DepartmentDto;
import net.javaguides.employeeservice.dto.EmployeeDto;
import net.javaguides.employeeservice.dto.OrganizationDto;
import net.javaguides.employeeservice.entity.Employee;
import net.javaguides.employeeservice.exception.ResourceNotFoundException;
import net.javaguides.employeeservice.repository.EmployeeRepository;
import net.javaguides.employeeservice.service.APIClient;
import net.javaguides.employeeservice.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);
   // private RestTemplate restTemplate;
    private WebClient webClient;
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
   // @CircuitBreaker(name="${spring.application.name}",fallbackMethod = "getDefaultDepartment")
    @Retry(name="${spring.application.name}",fallbackMethod = "getDefaultDepartment")
    public ApiResponseDto getEmployeeById(Long employeeId) {

      LOGGER.info("Inside getEmployeeId() method");

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
      DepartmentDto departmentDto = webClient.get()
                                .uri("http://localhost:8080/api/departments/"+employee.getDepartmentCode())
                                .retrieve()
                                .bodyToMono(DepartmentDto.class)
                                .block();

        OrganizationDto organizationDto = webClient.get()
                                .uri("http://localhost:8083/api/organizations/"+employee.getOrganizationCode())
                                .retrieve()
                                .bodyToMono(OrganizationDto.class)
                                .block();

    //  DepartmentDto departmentDto= apiClient.getDepartmentByCode(employee.getDepartmentCode());

      EmployeeDto employeeDto = modelMapper.map(employee,EmployeeDto.class);

      ApiResponseDto apiResponseDto = new ApiResponseDto(
                                                        employeeDto,
                                                        departmentDto,
                                                        organizationDto
                                                        );

      return apiResponseDto;
    }

    public ApiResponseDto getDefaultDepartment(Long employeeId, Exception exception) {

        LOGGER.info("Inside getDefaultDepartment() method");
        Employee employee =  employeeRepository.findById(employeeId)
                .orElseThrow( ()-> new ResourceNotFoundException(
                                "Employee",
                                "Employee Id",
                                employeeId
                        )
                );
      DepartmentDto departmentDto = new DepartmentDto();
      departmentDto.setDepartmentCode("RD001");
      departmentDto.setDepartmentDescription("Research & Development department");
      departmentDto.setDepartmentName("R&D Department");

      OrganizationDto organizationDto = new OrganizationDto();
      organizationDto.setOrganizationName("Dump Organization");
      organizationDto.setOrganizationDescription("Dummy Organization");
      organizationDto.setOrganizationCode("ORG-000");
      organizationDto.setCreationDate(LocalDateTime.now());

      EmployeeDto employeeDto = modelMapper.map(employee,EmployeeDto.class);

        ApiResponseDto apiResponseDto = new ApiResponseDto(
                employeeDto,
                departmentDto,
                organizationDto
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
