package net.javaguides.employeeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto {

    private EmployeeDto employeeDto;
    private DepartmentDto departmentDto;
    private OrganizationDto organizationDto;
}
