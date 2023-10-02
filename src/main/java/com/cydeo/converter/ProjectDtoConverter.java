package com.cydeo.converter;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.service.ProjectService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

//we have converters because we receive String from UI
@Component
@ConfigurationPropertiesBinding
public class ProjectDtoConverter implements Converter<String, ProjectDTO> {

    ProjectService projectService;

    public ProjectDtoConverter(@Lazy ProjectService projectService) {   //@Lazy to prevent circular dependency
        this.projectService = projectService;
    }

    @Override
    public ProjectDTO convert(String source) {

        if (source == null || source.equals("")) {
            return null;
        }

        return projectService.getByProjectCode(source);
        //converter using getByProjectCode() method from projectService interface
        //when we forget to type the implementation (ProjectServiceImpl) of this method (and leave null), we get error

    }

}
