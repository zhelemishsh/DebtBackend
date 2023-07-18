package sas.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AccountDto {
    public AccountDto(int id, String username) {
        this.id = id;
        this.username = username;
    }

    @JsonProperty("id")
    private int id;
    @JsonProperty("username")
    private String username;
}
