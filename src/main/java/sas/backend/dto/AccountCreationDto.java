package sas.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AccountCreationDto {
    public AccountCreationDto(
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
}
