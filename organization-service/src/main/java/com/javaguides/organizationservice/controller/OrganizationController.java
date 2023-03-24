package com.javaguides.organizationservice.controller;

import com.javaguides.organizationservice.dto.OrganizationDto;
import com.javaguides.organizationservice.entity.Organization;
import com.javaguides.organizationservice.service.Impl.OrganizationServiceImpl;
import com.javaguides.organizationservice.service.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizations")
@AllArgsConstructor
public class OrganizationController {

    private OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationDto> saveOrganization(@RequestBody OrganizationDto organizationDto){

        OrganizationDto savedOrganization =   organizationService.saveOrganization(organizationDto);

        return new ResponseEntity<>(savedOrganization, HttpStatus.CREATED);
    }

    @GetMapping("{orgCode}")
    public ResponseEntity<OrganizationDto> getOrganizationByCode(@PathVariable ("orgCode") String organizationCode){

       OrganizationDto organizationDto = organizationService.getOrganizationByCode(organizationCode);

       return  new ResponseEntity<>(organizationDto,HttpStatus.OK);
    }
}
