package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.ProjectService;
import com.cydeo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String createProject(Model model) {

        model.addAttribute("project", new ProjectDTO());
        model.addAttribute("projects", projectService.listAllProjectDetails());
        model.addAttribute("managers", userService.listAllByRole("manager"));

        return "/project/create";

    }

    @PostMapping("/create")
    public String insertProject(@ModelAttribute("project") ProjectDTO project, BindingResult bindingResult, Model model) {  // TODO: 03/10/2023 @Valid deleted, add again

        if (bindingResult.hasErrors()) {

            model.addAttribute("projects", projectService.listAllProjectDetails());
            model.addAttribute("managers", userService.listAllByRole("manager"));

            return "/project/create";

        }

        projectService.save(project);
        return "redirect:/project/create";

    }

    @GetMapping("/delete/{projectcode}")
    public String deleteProject(@PathVariable("projectcode") String projectcode) {
        projectService.delete(projectcode);
        return "redirect:/project/create";
    }

    @GetMapping("/complete/{projectcode}")
    public String completeProject(@PathVariable("projectcode") String projectcode) {
        projectService.complete(projectcode);
        return "redirect:/project/create";
    }

    @GetMapping("/update/{projectcode}")
    public String editProject(@PathVariable("projectcode") String projectcode, Model model) {

        model.addAttribute("project", projectService.getByProjectCode(projectcode));
        model.addAttribute("projects", projectService.listAllProjectDetails());
        model.addAttribute("managers", userService.listAllByRole("manager"));

        return "/project/update";

    }

    @PostMapping("/update")
    public String updateProject(@ModelAttribute("project") ProjectDTO project, BindingResult bindingResult, Model model) {  // TODO: 03/10/2023 @Valid deleted, add again

        if (bindingResult.hasErrors()) {

            model.addAttribute("projects", projectService.listAllProjectDetails());
            model.addAttribute("managers", userService.listAllByRole("manager"));

            return "/project/update";

        }

        projectService.update(project);
        return "redirect:/project/create";

    }

    @GetMapping("/manager/project-status")
    public String getProjectByManager(Model model) {
        //a manager is supposed to be logged in, and he/she is checking the project status
        //we don't have the security yet, so, we hard code that part here
//        UserDTO manager = userService.findById("john@cydeo.com");

        List<ProjectDTO> projects = projectService.listAllProjectDetails();

        model.addAttribute("projects", projects);

        return "/manager/project-status";
    }

//    @GetMapping("/manager/complete/{projectCode}")
//    public String managerCompleteProject(@PathVariable("projectCode") String projectCode) {
//        projectService.complete(projectService.findById(projectCode));
//        return "redirect:/project/manager/project-status";
//    }

}
