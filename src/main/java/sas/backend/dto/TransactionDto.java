package sas.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;

@Getter
public class TransactionDto {
    public TransactionDto() {}
    public TransactionDto(
            int id, String senderUsername, String receiverUsername, String description, Date date, double amount) {
        this.id = id;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.description = description;
        this.date = date;
        this.amount = amount;
    }

    @JsonProperty("id")
    private int id;
    @JsonProperty("sender_username")
    private String senderUsername;
    @JsonProperty("receiver_username")
    private String receiverUsername;
    @JsonProperty("description")
    private String description;
    @JsonProperty("date")
    private Date date;
    @JsonProperty("amount")
    private double amount;
}
