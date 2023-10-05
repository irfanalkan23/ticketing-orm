package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        //ProjectDtoConverter using getByProjectCode() method from projectService interface
        //when we forget to type the implementation (ProjectServiceImpl) of this method (and leave null), we get error

        return projectMapper.convertToDto(projectRepository.findByProjectCode(code));
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        return projectRepository.findAll().stream().map(projectMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO dto) {
        //Project Create form doesn't get the Status input, but Project List needs Status field
        dto.setProjectStatus(Status.OPEN);
        Project project = projectMapper.convertToEntity(dto);
        projectRepository.save(project);
    }

    @Override
    public void update(ProjectDTO dto) {
        //whatever not coming from dto, but database (entity) needs, we need to set those values

        //get the project (we need id) from database. dto doesn't have id.
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());
        //convert dto to entity
        Project convertedProject = projectMapper.convertToEntity(dto);
        //set the id to the convertedProject
        convertedProject.setId(project.getId());
        //set the project status, also not coming from the form
        convertedProject.setProjectStatus(project.getProjectStatus());
        //save it
        projectRepository.save(convertedProject);
    }

    @Override
    public void delete(String code) {
        Project project = projectRepository.findByProjectCode(code);
        project.setIsDeleted(true);
        //when we delete a project, the unique project code needs to be usable by another project
        project.setProjectCode(project.getProjectCode() + "-" + project.getId());
        projectRepository.save(project);

        //when a project is deleted, all the tasks of that project should be deleted as well
        taskService.deleteByProject(projectMapper.convertToDto(project));
    }

    @Override
    public void complete(String projectCode) {
        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);

        //when a project is deleted, all the tasks of that project should be deleted as well
        taskService.completeByProject(projectMapper.convertToDto(project));
    }

    //list all the projects assigned to a certain manager
    @Override
    public List<ProjectDTO> listAllProjectDetails() {
        UserDTO currentUserDTO = userService.findByUsername("mike@gmail.com");  //harold will come from security
        User user = userMapper.convertToEntity(currentUserDTO);
        List<Project> list = projectRepository.findAllByAssignedManager(user);
        //this list doesn't have any task counts. following code assigns

        return list.stream().map(project -> {
            ProjectDTO obj = projectMapper.convertToDto(project);
            //this ProjectDTO obj does NOT have completeTaskCounts and unfinishedTaskCounts fields

            obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
            obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));
            return obj;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> realAllByAssignedManager(User assignedManager) {
        List<Project> list = projectRepository.findAllByAssignedManager(assignedManager);
        return list.stream().map(projectMapper::convertToDto).collect(Collectors.toList());
    }
}











