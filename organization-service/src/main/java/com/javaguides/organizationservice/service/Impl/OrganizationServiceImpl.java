package com.javaguides.organizationservice.service.Impl;

import com.javaguides.organizationservice.dto.OrganizationDto;
import com.javaguides.organizationservice.entity.Organization;
import com.javaguides.organizationservice.mapper.OrganizationMapper;
import com.javaguides.organizationservice.repository.OrganizationRepository;
import com.javaguides.organizationservice.service.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private OrganizationRepository organizationRepository;
    @Override
    public OrganizationDto saveOrganization(OrganizationDto organizationDto) {

        Organization organization = OrganizationMapper.mapToOrganization(organizationDto);

        organizationRepository.save(organization);

        return  OrganizationMapper.mapToOrganizationDto(organization);

    }

    @Override
    public OrganizationDto getOrganizationByCode(String organizationCode) {

         Organization organization= organizationRepository.findByOrganizationCode(organizationCode);

         return OrganizationMapper.mapToOrganizationDto(organization);
    }
}
