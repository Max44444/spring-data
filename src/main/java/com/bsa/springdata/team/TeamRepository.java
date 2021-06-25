package com.bsa.springdata.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE Team t SET t.technology = (" +
            "   SELECT tech FROM Technology tech WHERE tech.name = :newTechnologyName) " +
            "WHERE t.id IN " +
            "   (SELECT te.id FROM Team te " +
            "   WHERE te.technology.name = :oldTechnologyName AND SIZE(te.users) < :devsNumber)")
    void updateTechnology(
            @Param("devsNumber") int devsNumber,
            @Param("oldTechnologyName") String oldTechnologyName,
            @Param("newTechnologyName") String newTechnologyName
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE teams AS t " +
            "SET name = CONCAT(t.name, '_', p.name, '_', tech.name) " +
            "FROM projects AS p, technologies AS tech " +
            "WHERE t.project_id = p.id AND tech.id = t.technology_id AND t.name = :hipsters",
            nativeQuery = true)
    void normalizeName(@Param("hipsters") String hipsters);

    int countByTechnologyName(String name);

    Optional<Team> findByName(String name);

}
