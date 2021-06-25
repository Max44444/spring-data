package com.bsa.springdata.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findByLastNameStartingWithIgnoreCase(String lastName, Pageable pageable);

    @Query("SELECT u FROM User u " +
            "JOIN u.office o " +
            "WHERE o.city = :city")
    List<User> findUserByCity(@Param("city") String city, Sort sort);

    List<User> findByExperienceGreaterThanEqualOrderByExperienceDesc(int experience);

    @Query("SELECT u FROM User u " +
            "JOIN u.team t " +
            "JOIN u.office o " +
            "WHERE t.room = :room AND o.city = :city")
    List<User> findUserByRoomAndCity(@Param("room") String room, @Param("city") String city, Sort sort);

    @Modifying
    @Query("DELETE FROM User u WHERE u.experience < :experience")
    int deleteUserByExperienceLessThan(@Param("experience") int experience);
}
