package sas.backend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {
    public Transaction() {}

    public Transaction(int id, Account sender, Account receiver, String description, Date date, double amount) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.description = description;
        this.date = date;
        this.amount = amount;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private Account sender;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id")
    private Account receiver;
    @Column(name = "description")
    private String description;
    @Column(name = "date")
    private Date date;
    @Column(name = "amount")
    private double amount;
}
