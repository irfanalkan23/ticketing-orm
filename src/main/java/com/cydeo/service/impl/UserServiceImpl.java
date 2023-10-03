package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, ProjectService projectService, TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        return userRepository.findAll(Sort.by("firstName")).stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

        @Override
    public UserDTO findByUsername(String username) {
        return userMapper.convertToDto(userRepository.findByUserName(username));
    }

    @Override
    public void save(UserDTO dto) {
        userRepository.save(userMapper.convertToEntity(dto));
    }

    @Override
    public UserDTO update(UserDTO dto) {    //this dto is updated userDto. returning UserDTO because we will use later in Security.
        //find current user
        User user = userRepository.findByUserName(dto.getUserName());

        //map updated user to entity object
        User convertedUser = userMapper.convertToEntity(dto);   //this doesn't have an id

        //set id to convertedUser object
        convertedUser.setId(user.getId());

        //save updated user (to database)
        userRepository.save(convertedUser);

        return null;
    }

    //hard delete. not used in the project.
    @Override
    public void deleteByUserName(String username) {
        userRepository.deleteByUserName(username);
    }

    //soft delete. used in this project.
    @Override
    public void delete(String username) {
        // I will not delete from db. I will just change the isDeleted flag (field) to "true"
        User user = userRepository.findByUserName(username);
        if (checkIfUserCanBeDeleted(user)){
            user.setIsDeleted(true);
            userRepository.save(user);
        }
        // TODO: 03/10/2023 we will add exceptions at API phase
    }

    private boolean checkIfUserCanBeDeleted(User user){
        switch (user.getRole().getDescription()){
            case "Manager":
                //delete (true) if this Manager has any project
                List<ProjectDTO> projectDTOList = projectService.realAllByAssignedManager(user);
                return projectDTOList.size() == 0;
            case "Employee":
                List<TaskDTO> taskDTOList = taskService.realAllByAssignedEmployee(user);
                return taskDTOList.size() == 0;
            default:
                return true;
        }
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        return userRepository.findAllByRoleDescriptionIgnoreCase(role).stream()
                .map(userMapper::convertToDto).collect(Collectors.toList());
    }
}
