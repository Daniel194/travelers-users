package org.travelers.users.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.travelers.users.config.Constants;
import org.travelers.users.service.UserService;
import org.travelers.users.service.dto.UserDTO;

@RestController
@RequestMapping("/api/user")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserDTO> getUser() {
        log.debug("REST request to get current User");
        return ResponseUtil.wrapOrNotFound(userService.getCurrent());
    }

    @GetMapping("/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<UserDTO> getUserByLogin(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(userService.getByLogin(login));
    }

}
