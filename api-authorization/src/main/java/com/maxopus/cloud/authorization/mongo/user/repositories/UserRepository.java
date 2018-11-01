package com.maxopus.cloud.authorization.mongo.user.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.maxopus.cloud.authorization.mongo.user.domain.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>, UserRepositoryBase {

	@Query(value = "{ 'username' : ?0 }")
	User findByUsername(String username);
}
