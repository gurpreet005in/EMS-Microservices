package net.javaguides.departmentservice.service.Impl;

import lombok.AllArgsConstructor;
import net.javaguides.departmentservice.dto.DepartmentDto;
import net.javaguides.departmentservice.entity.Department;
import net.javaguides.departmentservice.exception.ResourceNotFoundException;
import net.javaguides.departmentservice.repository.DepartmentRepository;
import net.javaguides.departmentservice.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;
    private ModelMapper modelMapper;

    @Override
    public DepartmentDto saveDepartment(DepartmentDto departmentDto) {

        Department department = modelMapper.map(departmentDto,Department.class);

        Department savedDepartment =  departmentRepository.save(department);

        DepartmentDto savedDepartmentDto = modelMapper.map(savedDepartment,DepartmentDto.class);

        return savedDepartmentDto;
    }

    @Override
    public DepartmentDto getDepartmentByCode(String departmentCode) {

        Department department = departmentRepository.findByDepartmentCode(departmentCode)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Department","Department Code",departmentCode)
                );

        DepartmentDto departmentDto = modelMapper.map(department,DepartmentDto.class);

        return  departmentDto;
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {

        List<Department> departments   =  departmentRepository.findAll();

        List<DepartmentDto> departmentDtoList
                   = departments.stream()
                    .map((department -> modelMapper.map(department,DepartmentDto.class)))
                    .collect(Collectors.toList());
        return departmentDtoList;
    }


}
