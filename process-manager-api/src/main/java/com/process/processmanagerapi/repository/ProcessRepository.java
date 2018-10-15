package com.process.processmanagerapi.repository;

import com.process.processmanagerapi.entity.Process;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  ProcessRepository extends CrudRepository<Process, Long> {

}
