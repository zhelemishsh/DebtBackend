package sas.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sas.backend.model.Account;
import sas.backend.model.Friendship;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
    List<Friendship> findFriendsByAccount(Account account);
    Friendship findFriendshipByAccountAndFriend(Account account, Account friend);
}
