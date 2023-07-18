package sas.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FriendRequestDto {
    public FriendRequestDto(
            @JsonProperty("request_id") int id,
            @JsonProperty("sender_username") String senderUsername) {
        this.id = id;
        this.senderUsername = senderUsername;
    }

    @JsonProperty("request_id")
    private int id;
    @JsonProperty("sender_username")
    private final String senderUsername;
}
