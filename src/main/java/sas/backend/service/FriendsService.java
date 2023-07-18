package sas.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sas.backend.dto.FriendDto;
import sas.backend.dto.FriendRequestDto;
import sas.backend.exception.FriendsServiceException;
import sas.backend.model.Account;
import sas.backend.model.Friendship;
import sas.backend.model.FriendRequest;
import sas.backend.model.Transaction;
import sas.backend.repository.AccountRepository;
import sas.backend.repository.FriendshipRepository;
import sas.backend.repository.FriendRequestRepository;
import sas.backend.repository.TransactionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendsService {
    private final FriendshipRepository friendshipRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Autowired
    public FriendsService(
            FriendshipRepository friendRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            FriendRequestRepository friendRequestRepository) {
        this.friendshipRepository = friendRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.friendRequestRepository = friendRequestRepository;
    }

    public List<FriendDto> getFriends(String accountUsername) throws FriendsServiceException {
        Account account = getAccountByUsername(accountUsername);
        List<Friendship> friends = friendshipRepository.findFriendsByAccount(account);
        return friends.stream().map(this::friendEntityToDto).collect(Collectors.toList());
    }

    public List<FriendRequestDto> getRequests(String accountUsername) throws FriendsServiceException {
        Account account = getAccountByUsername(accountUsername);
        List<FriendRequest> requests = friendRequestRepository.findByReceiver(account);
        return requests.stream().map(this::requestEntityToDto).collect(Collectors.toList());
    }

    public void sendFriendRequest(String senderUsername, String receiverUsername) throws FriendsServiceException {
        Account senderAccount = getAccountByUsername(senderUsername);
        Account receiverAccount = getAccountByUsername(receiverUsername);
        if (friendshipRepository.findFriendshipByAccountAndFriend(senderAccount, receiverAccount) != null) {
            throw new FriendsServiceException(senderUsername + " already have " + receiverUsername + " in friends");
        }
        if (friendRequestRepository.findBySenderAndReceiver(senderAccount, receiverAccount) != null) {
            throw new FriendsServiceException(senderUsername + " already sent request to " + receiverUsername);
        }
        FriendRequest existingRequest = friendRequestRepository.findBySenderAndReceiver(receiverAccount, senderAccount);
        if (existingRequest != null) {
            friendshipRepository.save(new Friendship(0, senderAccount, receiverAccount, receiverUsername));
            friendshipRepository.save(new Friendship(0, receiverAccount, senderAccount, senderUsername));
            friendRequestRepository.delete(existingRequest);
        }
        else {
            friendRequestRepository.save(new FriendRequest(0, senderAccount, receiverAccount));
        }
    }

    public void deleteFriend(String accountUsername, String deletedFriendUsername) throws FriendsServiceException {
        Account account = getAccountByUsername(accountUsername);
        Account deletedFriendAccount = getAccountByUsername(deletedFriendUsername);
        Friendship deletedFriendship = friendshipRepository.findFriendshipByAccountAndFriend(
                account, deletedFriendAccount);
        if (deletedFriendship == null) {
            throw new FriendsServiceException(accountUsername + " is not friend of " + deletedFriendUsername);
        }
        friendshipRepository.delete(deletedFriendship);
        friendshipRepository.delete(friendshipRepository.findFriendshipByAccountAndFriend(
                deletedFriendAccount, account));
    }

    public void rejectRequest(String receiverUsername, String senderUsername) throws FriendsServiceException {
        Account receiverAccount = getAccountByUsername(receiverUsername);
        Account senderAccount = getAccountByUsername(senderUsername);
        FriendRequest request = friendRequestRepository.findBySenderAndReceiver(senderAccount, receiverAccount);
        if (request == null) {
            throw new FriendsServiceException(senderUsername + " did not send request to " + receiverUsername);
        }
        friendRequestRepository.delete(request);
    }

    public void renameFriend(String accountUsername, String friendUsername, String newName)
            throws FriendsServiceException {
        Account account = getAccountByUsername(accountUsername);
        Account friendAccount = getAccountByUsername(friendUsername);
        Friendship friendship = friendshipRepository.findFriendshipByAccountAndFriend(
                account, friendAccount);
        if (friendship == null) {
            throw new FriendsServiceException(accountUsername + " is not friend of " + friendUsername);
        }
        friendship.setName(newName);
        friendshipRepository.save(friendship);
    }

    public FriendDto getFriend(String accountUsername, String friendUsername) throws FriendsServiceException {
        Account account = getAccountByUsername(accountUsername);
        Account friendAccount = getAccountByUsername(friendUsername);
        Friendship friendship = friendshipRepository.findFriendshipByAccountAndFriend(
                account, friendAccount);
        if (friendship == null) {
            throw new FriendsServiceException(accountUsername + " is not friend of " + friendUsername);
        }
        return friendEntityToDto(friendship);
    }

    private Account getAccountByUsername(String username) throws FriendsServiceException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new FriendsServiceException("Account by " + username + " does not exist");
        }
        return account;
    }

    private FriendDto friendEntityToDto(Friendship friendship) {
        double sentAmount = transactionRepository.findBySenderAndReceiver(
                friendship.getAccount(), friendship.getFriend()).stream()
                .mapToDouble(Transaction::getAmount).sum();
        double receivedAmount = transactionRepository.findBySenderAndReceiver(
                friendship.getFriend(), friendship.getAccount()).stream()
                .mapToDouble(Transaction::getAmount).sum();
        return new FriendDto(
                friendship.getFriend().getId(),
                friendship.getFriend().getUsername(),
                friendship.getName(),
                sentAmount - receivedAmount);
    }

    private FriendRequestDto requestEntityToDto(FriendRequest request) {
        return new FriendRequestDto(request.getId(), request.getSender().getUsername());
    }
}
