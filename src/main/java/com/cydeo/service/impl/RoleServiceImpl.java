package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDTO> listAllRoles() {
        //controller is asking for list of roles (RoleDTO)
        //but repository is providing entity (Role)
        //we need mapper

        return null;
    }

    @Override
    public RoleDTO findById(Long id) {
        return null;
    }
}
