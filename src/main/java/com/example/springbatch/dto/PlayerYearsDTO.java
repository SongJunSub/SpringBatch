package com.example.springbatch.dto;

import lombok.Data;

@Data
public class PlayerYearsDTO {

    private String id;

    private String lastName;

    private String firstName;

    private String position;

    private int birthYear;

    private int debutYear;

}