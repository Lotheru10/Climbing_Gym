package com.project.climbinggym.repository;

import com.project.climbinggym.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Override
    boolean existsById(String s);

    @Override
    void deleteById(String s);

    List<User> findByLastname(String lastname);

}
