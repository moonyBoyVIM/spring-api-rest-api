package com.moonyboyvim.runnerz.run;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface RunRepository extends ListCrudRepository<Run, Integer> {
    @Query("select * from run where location = :location")
    List<Run> findAllByLocation(String location);
}
