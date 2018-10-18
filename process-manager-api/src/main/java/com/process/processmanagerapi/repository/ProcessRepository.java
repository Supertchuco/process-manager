package com.process.processmanagerapi.repository;

import com.process.processmanagerapi.entity.Process;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  ProcessRepository extends CrudRepository<Process, Long> {

    Process findByProcessNumber(final int processNumber);

    List<Process> findAll();
}
