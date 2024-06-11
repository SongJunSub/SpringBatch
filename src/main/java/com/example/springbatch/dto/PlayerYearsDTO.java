package com.example.springbatch.dto;

import lombok.Data;

import java.time.Year;

@Data
public class PlayerYearsDTO {

    private String id;

    private String lastName;

    private String firstName;

    private String position;

    private int birthYear;

    private int debutYear;

    private int yearsExperience;

    public PlayerYearsDTO(PlayerDTO playerDTO) {
        this.id = playerDTO.getId();
        this.lastName = playerDTO.getLastName();
        this.firstName = playerDTO.getFirstName();
        this.position = playerDTO.getPosition();
        this.birthYear = playerDTO.getBirthYear();
        this.debutYear = playerDTO.getDebutYear();
        this.yearsExperience = Year.now().getValue() - playerDTO.getDebutYear();
    }

}