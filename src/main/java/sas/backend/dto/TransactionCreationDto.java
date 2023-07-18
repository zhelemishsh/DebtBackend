package sas.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TransactionCreationDto {
    public TransactionCreationDto(
            @JsonProperty("description") String description,
            @JsonProperty("amount") double amount) {
        this.description = description;
        this.amount = amount;
    }

    @JsonProperty("description")
    private String description;
    @JsonProperty("amount")
    private double amount;
}
