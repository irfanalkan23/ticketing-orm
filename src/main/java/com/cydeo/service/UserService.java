package com.cydeo.service;


import com.cydeo.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> listAllUsers();
    //UserDTO, not User (entity), because this method is called by UserController (view),
    //means, Service layer is always DTO

    //Update button needs;
    UserDTO findByUsername(String username);

    //Create button needs;
    void save(UserDTO dto);

    UserDTO update(UserDTO dto);
    void deleteByUserName(String username);

    void delete(String username);

}
