package com.moonyboyvim.runnerz.run;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/runs")
public class RunController {
    private final RunRepository runRepository;

    public RunController(RunRepository runRepository) {
        this.runRepository = runRepository;
    }

    // GET method
    @GetMapping
    List<Run> findAll() {
        return runRepository.findAll();
    }

    // GET method
    @GetMapping("/{id}")
    Run findById(@PathVariable Integer id) {
        Optional<Run> run = runRepository.findById(id);
        if (run.isEmpty()) {
            throw new RunNotFoundException();
        }
        return run.get();
    }

    // GET method
    @GetMapping("/location/{location}")
    List<Run> findAllByLocation(@PathVariable String location) {
        return runRepository.findAllByLocation(location);
    }

    // POST method
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void createRun(@Valid @RequestBody Run run) {
        runRepository.save(run);
    }

    //PUT method
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void updateRun(@Valid @RequestBody Run run, @PathVariable Integer id) {
        runRepository.save(run);
    }

    //DELETE method
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteRun(@PathVariable Integer id) {
        runRepository.delete(runRepository.findById(id).get());
    }
}