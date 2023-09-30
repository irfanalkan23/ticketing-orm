package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import com.cydeo.mapper.RoleMapper;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<RoleDTO> listAllRoles() {
        //controller is asking for list of roles (RoleDTO)
        //but repository is providing entity (Role)
        //we need mapper

        //convert entity to dto - Mapper
        //get roles from db and convert each role to roleDto
        return roleRepository.findAll().stream().map(roleMapper::convertToDto).collect(Collectors.toList());
        //map(obj -> roleMapper.convertToDto(obj))
    }

    @Override
    public RoleDTO findById(Long id) {
        return null;
    }
}
