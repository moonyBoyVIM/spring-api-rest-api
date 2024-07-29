package com.moonyboyvim.runnerz.run;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@JdbcTest
@Import(JdbcClientRunRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcClientRunRepositoryTest {

    @Autowired
    JdbcClientRunRepository repository;

    @BeforeEach
    void setUp(){
        repository.create(new Run(
                1,
                "Monday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.HOURS),
                3,
                Location.INDOOR
        ));
        repository.create(new Run(
                2,
                "Wednesday Evening Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(60, ChronoUnit.MINUTES),
                6,
                Location.INDOOR
        ));
    }
    @Test
    void shouldFindAllRuns() {
        List<Run> runs = repository.findAll();
        assertEquals(2, runs.size(), "Should have returned 2 runs!");
    }

    @Test
    void shouldFindRunWithValidId() {
        Run run = repository.findById(1).get();
        assertEquals("Monday Morning Run", run.title());
        assertEquals(3, run.miles());
    }

    @Test
    void shouldNotFindWithInvalidId() {
        Optional<Run> run = repository.findById(3);
        assertTrue(run.isEmpty());
    }

    @Test
    void shouldCreateNewRun() {
        repository.create(new Run(
                3,
                "Friday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                3,
                Location.INDOOR
        ));
        List<Run> runs = repository.findAll();
        assertEquals(3, runs.size());
    }

    @Test
    void shouldUpdateRun() {
        repository.update(new Run(
                1,
                "Monday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.MINUTES),
                5,
                Location.OUTDOOR
        ), 1);
        Run run = repository.findById(1).get();
        assertEquals("Monday Morning Run", run.title());
        assertEquals(5, run.miles());
        assertEquals(Location.OUTDOOR, run.location());
    }

    @Test
    void shouldDeleteRun() {
        repository.delete(1);
        List<Run> runs = repository.findAll();
        assertEquals(1, runs.size());
    }
}