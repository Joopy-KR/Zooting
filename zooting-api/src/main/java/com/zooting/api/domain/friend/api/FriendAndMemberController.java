package com.zooting.api.domain.friend.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@PreAuthorize("hasAnyRole('USER')")
@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendAndMemberController {

}
