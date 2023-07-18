package sas.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sas.backend.model.Account;
import sas.backend.model.FriendRequest;
import sas.backend.model.Transaction;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
    FriendRequest findBySenderAndReceiver(Account sender, Account receiver);
    List<FriendRequest> findByReceiver(Account receiver);
}