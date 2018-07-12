package com.bridgelabz.fundoonotes.user.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bridgelabz.fundoonotes.user.models.User;

public interface UserRepository extends MongoRepository<User, String>{

}
