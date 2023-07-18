package sas.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.sql.Date;

@Getter
public class FriendDto {
    public FriendDto(int id, String username, String name, double debt) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.debt = debt;
    }

    @JsonProperty("account_id")
    private int id;
    @JsonProperty("username")
    private final String username;
    @JsonProperty("name")
    private final String name;
    @JsonProperty("debt")
    private double debt;
}
