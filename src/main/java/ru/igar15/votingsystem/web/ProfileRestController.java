package ru.igar15.votingsystem.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.igar15.votingsystem.model.User;
import ru.igar15.votingsystem.service.UserService;
import ru.igar15.votingsystem.to.UserTo;
import ru.igar15.votingsystem.util.UserUtil;

import java.net.URI;

import static ru.igar15.votingsystem.util.ValidationUtil.assureIdConsistent;
import static ru.igar15.votingsystem.util.ValidationUtil.checkNew;
import static ru.igar15.votingsystem.web.SecurityUtil.authUserId;

@RestController
@RequestMapping(value = ProfileRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/rest/profile";

    @Autowired
    private UserService service;

    @GetMapping
    public User get() {
        int userId = authUserId();
        log.info("get {}", userId);
        return service.get(userId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        int userId = authUserId();
        log.info("delete {}", userId);
        service.delete(userId);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@RequestBody UserTo userTo) {
        log.info("create {}", userTo);
        checkNew(userTo);
        User created = service.create(UserUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }


    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody UserTo userTo) {
        int userId = authUserId();
        log.info("update {} with id={}", userTo, userId);
        assureIdConsistent(userTo, userId);
        service.update(userTo);
    }
}