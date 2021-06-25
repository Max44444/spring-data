package com.bsa.springdata.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Role r WHERE r.code = :code AND SIZE(r.users) = 0")
    void deleteRoleByCodeIfNoUsers(@Param("code") String code);

}
