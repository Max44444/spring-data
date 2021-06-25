package com.bsa.springdata.office;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfficeRepository extends JpaRepository<Office, UUID> {

    @Query("SELECT DISTINCT o FROM Office o " +
            "JOIN o.users u " +
            "JOIN u.team t " +
            "JOIN t.technology tech " +
            "WHERE tech.name = :technologyName")
    List<Office> findOfficeByTechnology(@Param("technologyName") String technologyName);

    @Modifying
    @Transactional
    @Query("UPDATE Office o " +
            "SET o.address = :newAddress " +
            "WHERE SIZE(o.users) <> 0 AND  o.address = :oldAddress")
    void updateOfficeAdders(@Param("oldAddress") String oldAddress, @Param("newAddress") String newAddress);

    Optional<Office> findOfficeByAddress(String address);

}
