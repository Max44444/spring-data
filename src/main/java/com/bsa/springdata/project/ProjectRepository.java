package com.bsa.springdata.project;

import com.bsa.springdata.project.dto.ProjectSummaryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @Query("SELECT p, COUNT(u) AS usersNumber FROM Project p " +
            "JOIN p.teams t " +
            "JOIN t.users u " +
            "WHERE t.technology.name = :technology " +
            "GROUP BY p.id " +
            "ORDER BY usersNumber DESC")
    List<Project> findTopProjectsByTechnology(@Param("technology") String technology, Pageable pageable);

    @Query("SELECT p, COUNT(t) AS teamsNumber, COUNT(u) AS usersNumber " +
            "FROM Project p " +
            "JOIN p.teams t " +
            "JOIN t.users u " +
            "GROUP BY p.id " +
            "ORDER BY teamsNumber DESC, usersNumber DESC, p.name DESC ")
    List<Project> findTheBiggestProject(Pageable pageable);

    @Query(value = "SELECT p.name, COUNT(DISTINCT t) AS teamsNumber, count(u) AS developersNumber, " +
            "STRING_AGG(DISTINCT t2.name, ',' ORDER BY t2.name DESC) AS technologies " +
            "FROM projects p " +
            "JOIN teams t ON p.id = t.project_id " +
            "JOIN users u ON t.id = u.team_id " +
            "JOIN technologies t2 ON t.technology_id = t2.id " +
            "GROUP BY p.name " +
            "ORDER BY p.name", nativeQuery = true)
    List<ProjectSummaryDto> getProjectsSummary();

    @Query("SELECT COUNT(DISTINCT p) FROM Project p " +
            "JOIN p.teams t " +
            "JOIN t.users u " +
            "JOIN u.roles r " +
            "WHERE r.name = :role ")
    int getCountWithRole(@Param("role") String role);

}