package sas.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sas.backend.dto.TransactionCreationDto;
import sas.backend.dto.TransactionDto;
import sas.backend.exception.TransactionsServiceException;
import sas.backend.model.Account;
import sas.backend.model.Transaction;
import sas.backend.repository.AccountRepository;
import sas.backend.repository.FriendshipRepository;
import sas.backend.repository.TransactionRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionsService {
    private final TransactionRepository transactionRepository;
    private final FriendshipRepository friendshipRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionsService(
            TransactionRepository transactionRepository,
            FriendshipRepository friendRepository,
            AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.friendshipRepository = friendRepository;
        this.accountRepository = accountRepository;
    }

    public List<TransactionDto> getTransactions(String accountUsername, String friendUsername)
            throws TransactionsServiceException {
        Account account = getAccountByUsername(accountUsername);
        Account friendAccount = getAccountByUsername(friendUsername);
        if (friendshipRepository.findFriendshipByAccountAndFriend(account, friendAccount) == null) {
            throw new TransactionsServiceException(accountUsername + " and " + friendUsername + " are not friends");
        }
        List<Transaction> transactions = transactionRepository.findBySenderAndReceiver(account, friendAccount);
        transactions.addAll(transactionRepository.findBySenderAndReceiver(friendAccount, account));
        return transactions.stream().map(this::transactionEntityToDto).collect(Collectors.toList());
    }

    public void makeTransaction(String accountUsername, String friendUsername, TransactionCreationDto transactionDto)
            throws TransactionsServiceException {
        Account account = getAccountByUsername(accountUsername);
        Account friendAccount = getAccountByUsername(friendUsername);
        if (friendshipRepository.findFriendshipByAccountAndFriend(account, friendAccount) == null) {
            throw new TransactionsServiceException(accountUsername + " and " + friendUsername + " are not friends");
        }
        transactionRepository.save(new Transaction(
                0, account, friendAccount, transactionDto.getDescription(), new Date(), transactionDto.getAmount()));
    }

    public void deleteTransaction(String senderUsername, int transactionId) throws TransactionsServiceException {
        Account sender = getAccountByUsername(senderUsername);
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (!transaction.isPresent()) {
            throw new TransactionsServiceException("Transaction by " + transactionId + " id does not exist");
        }
        if (transaction.get().getSender() != sender) {
            throw new TransactionsServiceException(senderUsername + " is not sender of transaction");
        }
        transactionRepository.delete(transaction.get());
    }

    private Account getAccountByUsername(String username) throws TransactionsServiceException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new TransactionsServiceException("Account by " + username + " does not exist");
        }
        return account;
    }

    private TransactionDto transactionEntityToDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getSender().getUsername(),
                transaction.getReceiver().getUsername(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getAmount());
    }
}
