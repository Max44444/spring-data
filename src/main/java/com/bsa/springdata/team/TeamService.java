package com.bsa.springdata.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TechnologyRepository technologyRepository;

    public void updateTechnology(int devsNumber, String oldTechnologyName, String newTechnologyName) {
        teamRepository.updateTechnology(devsNumber, oldTechnologyName, newTechnologyName);
    }

    public void normalizeName(String hipsters) {
        teamRepository.normalizeName(hipsters);
    }

}
