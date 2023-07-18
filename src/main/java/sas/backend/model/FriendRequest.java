package sas.backend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "friend_requests")
public class FriendRequest {
    public FriendRequest() {}

    public FriendRequest(int id, Account sender, Account receiver) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
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
}
