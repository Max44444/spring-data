package com.bsa.springdata.user;

import com.bsa.springdata.office.OfficeRepository;
import com.bsa.springdata.team.TeamRepository;
import com.bsa.springdata.user.dto.CreateUserDto;
import com.bsa.springdata.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private TeamRepository teamRepository;

    public Optional<UUID> safeCreateUser(CreateUserDto userDto) {
        var office = officeRepository.findById(userDto.getOfficeId());
        var team = teamRepository.findById((userDto.getTeamId()));

        return office.flatMap(o -> team.map(t -> {
            var user = User.fromDto(userDto, o, t);
            var result = userRepository.save(user);
            return result.getId();
        }));
    }

    public Optional<UUID> createUser(CreateUserDto userDto) {
        try {
            var office = officeRepository.getOne(userDto.getOfficeId());
            var team = teamRepository.getOne(userDto.getTeamId());

            var user = User.fromDto(userDto, office, team);
            var result = userRepository.save(user);
            return Optional.of(result.getId());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<UserDto> getUserById(UUID id) {
        return userRepository.findById(id).map(UserDto::fromEntity);
    }

    public List<UserDto> getUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<UserDto> findByLastName(String lastName, int page, int size) {
        var sort = Sort.sort(User.class).by(User::getLastName).ascending();
        var pageable = PageRequest.of(page, size, sort);

        return userRepository.findByLastNameStartingWithIgnoreCase(lastName, pageable)
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<UserDto> findByCity(String city) {
        var sort = Sort.sort(User.class).by(User::getLastName).ascending();

        return userRepository.findUserByCity(city, sort)
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<UserDto> findByExperience(int experience) {
        return userRepository.findByExperienceGreaterThanEqualOrderByExperienceDesc(experience)
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<UserDto> findByRoomAndCity(String city, String room) {
        var sort = Sort.sort(User.class).by(User::getLastName).ascending();

        return userRepository.findUserByRoomAndCity(room, city, sort)
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    public int deleteByExperience(int experience) {
        return userRepository.deleteUserByExperienceLessThan(experience);
    }

}
