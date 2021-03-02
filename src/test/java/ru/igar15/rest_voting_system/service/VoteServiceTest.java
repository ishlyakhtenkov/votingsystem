package ru.igar15.rest_voting_system.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.igar15.rest_voting_system.model.Vote;
import ru.igar15.rest_voting_system.repository.VoteRepository;
import ru.igar15.rest_voting_system.util.exception.VoteUpdateForbiddenException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.Assert.assertThrows;
import static ru.igar15.rest_voting_system.RestaurantTestData.RESTAURANT1_ID;
import static ru.igar15.rest_voting_system.RestaurantTestData.RESTAURANT2_ID;
import static ru.igar15.rest_voting_system.UserTestData.USER1_ID;
import static ru.igar15.rest_voting_system.VoteTestData.*;

public class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    private VoteService service;

    @Autowired
    private VoteRepository repository;

    @Test
    public void create() {
        Vote created = service.registerVote(RESTAURANT1_ID, USER1_ID, LocalDate.now(), LocalTime.now());
        int newId = created.id();
        Vote newVote = getNew();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(repository.findByIdAndUser_Id(newId, USER1_ID).get(), newVote);
    }

    @Test
    public void update() {
        service.registerVote(RESTAURANT2_ID, USER1_ID, LocalDate.of(2021, Month.FEBRUARY, 25), BEFORE_ELEVEN);
        VOTE_MATCHER.assertMatch(repository.findByIdAndUser_Id(VOTE1_ID, USER1_ID).get(), getUpdated());
    }

    @Test
    public void updateFailed() {
        assertThrows(VoteUpdateForbiddenException.class,
                () -> service.registerVote(RESTAURANT2_ID, USER1_ID, LocalDate.of(2021, Month.FEBRUARY, 25), AFTER_ELEVEN));
    }
}