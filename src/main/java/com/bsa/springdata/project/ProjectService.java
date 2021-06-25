package com.bsa.springdata.project;

import com.bsa.springdata.project.dto.CreateProjectRequestDto;
import com.bsa.springdata.project.dto.ProjectDto;
import com.bsa.springdata.project.dto.ProjectSummaryDto;
import com.bsa.springdata.team.Team;
import com.bsa.springdata.team.TeamRepository;
import com.bsa.springdata.team.Technology;
import com.bsa.springdata.team.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TechnologyRepository technologyRepository;
    @Autowired
    private TeamRepository teamRepository;

    public List<ProjectDto> findTop5ByTechnology(String technology) {
        var pageable = PageRequest.of(0, 5);

        return projectRepository.findTopProjectsByTechnology(technology, pageable)
                .stream()
                .map(ProjectDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<ProjectDto> findTheBiggest() {
        var pageable = PageRequest.of(0, 1);

        return Optional.of(ProjectDto.fromEntity(projectRepository.findTheBiggestProject(pageable).get(0)));
    }

    public List<ProjectSummaryDto> getSummary() {
        return projectRepository.getProjectsSummary();
    }

    public int getCountWithRole(String role) {
        return projectRepository.getCountWithRole(role);
    }

    public UUID createWithTeamAndTechnology(CreateProjectRequestDto createProjectRequest) {

        var technology = Technology.builder()
                .name(createProjectRequest.getTech())
                .description(createProjectRequest.getTechDescription())
                .link(createProjectRequest.getTechLink())
                .build();

        var team = Team.builder()
                .name(createProjectRequest.getTeamName())
                .room(createProjectRequest.getTeamRoom())
                .area(createProjectRequest.getTeamArea())
                .technology(technology)
                .build();

        var project = Project.builder()
                .name(createProjectRequest.getProjectName())
                .description(createProjectRequest.getProjectDescription())
                .teams(List.of(team))
                .build();

        return projectRepository.save(project).getId();
    }

}
