package com.moonyboyvim.runnerz.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RunController.class)
class RunControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RunRepository repository;

    private final List<Run> runs = new ArrayList<>();

    @BeforeEach
    void setUp() {
        runs.add(new Run(
                1,
                "Monday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(30, ChronoUnit.HOURS),
                3,
                Location.INDOOR));
    }

    @Test
    void shouldFindAllRuns() throws Exception {
        when(repository.findAll()).thenReturn(runs);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/runs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(runs.size())));
    }

    @Test
    void shouldFindOneRun() throws Exception {
        Run run = runs.get(0);
        when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(run));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/runs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(run.id())))
                .andExpect(jsonPath("$.title", is(run.title())))
                .andExpect(jsonPath("$.miles", is(run.miles())))
                .andExpect(jsonPath("$.location", is(run.location().toString())));
    }

    @Test
    void shouldReturnNotFoundWithInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/runs/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewRun() throws Exception {
        Run run = new Run(null, "test", LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS), 1, Location.INDOOR);
        mockMvc.perform(post("/api/runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(run)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateRun() throws Exception {
        Run run = new Run(null, "test", LocalDateTime.now(), LocalDateTime.now().plus(2, ChronoUnit.HOURS), 1, Location.INDOOR);
        mockMvc.perform(put("/api/runs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(run)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteRun() throws Exception {
        mockMvc.perform(delete("/api/runs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(objectMapper.writeValueAsString(runs.get(0))))
                .andExpect(status().isNoContent());
    }
}