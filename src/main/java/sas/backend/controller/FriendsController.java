package sas.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sas.backend.dto.FriendDto;
import sas.backend.dto.FriendRequestDto;
import sas.backend.exception.FriendsServiceException;
import sas.backend.service.FriendsService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/friends")
public class FriendsController {
    private final FriendsService friendsService;

    @Autowired
    public FriendsController(FriendsService friendsService) {
        this.friendsService = friendsService;
    }

    @GetMapping("/get")
    public List<FriendDto> getFriends() throws FriendsServiceException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return friendsService.getFriends(username);
    }

    @GetMapping("/get/{friendUsername}")
    public FriendDto getFriend(@PathVariable String friendUsername) throws FriendsServiceException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return friendsService.getFriend(username, friendUsername);
    }

    @GetMapping("/requests/get")
    public List<FriendRequestDto> getRequests() throws FriendsServiceException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return friendsService.getRequests(username);
    }

    @PostMapping("/requests/send/{receiverUsername}")
    public void sendFriendRequest(@PathVariable String receiverUsername) throws FriendsServiceException {
        String accountUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        friendsService.sendFriendRequest(accountUsername, receiverUsername);
    }

    @PostMapping("/requests/reject/{senderUsername}")
    public void rejectRequest(@PathVariable String senderUsername) throws FriendsServiceException {
        String accountUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        friendsService.rejectRequest(accountUsername, senderUsername);
    }

    @DeleteMapping("delete/{username}")
    public void deleteFriend(@PathVariable String username) throws FriendsServiceException {
        String accountUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        friendsService.deleteFriend(accountUsername, username);
    }

    @PutMapping("/rename/{friendUsername}/{newName}")
    public void renameFriend(@PathVariable String friendUsername, @PathVariable String newName)
            throws FriendsServiceException {
        String accountUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        friendsService.renameFriend(accountUsername, friendUsername, newName);
    }
}
