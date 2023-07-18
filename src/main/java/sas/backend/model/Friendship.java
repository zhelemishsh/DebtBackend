package sas.backend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "friendships")
public class Friendship {
    public Friendship() {}

    public Friendship(int id, Account account, Account friend, String name) {
        this.id = id;
        this.account = account;
        this.friend = friend;
        this.name = name;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "friend_account_id")
    private Account friend;
    @Column(name = "name")
    private String name;
}
