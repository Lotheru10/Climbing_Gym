package com.project.climbinggym.repository;

import com.project.climbinggym.model.EntryType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntryTypeRepository extends MongoRepository<EntryType, String> {

    Optional<EntryType> findByEntryType(String entryType);
}
