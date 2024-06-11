package com.example.springbatch.job.filedatareadwrite;

import com.example.springbatch.dto.PlayerDTO;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class PlayerFieldSetMapper implements FieldSetMapper<PlayerDTO> {

    public PlayerDTO mapFieldSet(FieldSet fieldSet) {
        PlayerDTO playerDTO = new PlayerDTO();

        playerDTO.setId(fieldSet.readString("id"));
        playerDTO.setLastName(fieldSet.readString("lastName"));
        playerDTO.setFirstName(fieldSet.readString("firstName"));
        playerDTO.setPosition(fieldSet.readString("position"));
        playerDTO.setBirthYear(fieldSet.readInt("birthYear"));
        playerDTO.setDebutYear(fieldSet.readInt("debutYear"));

        return playerDTO;
    }

}