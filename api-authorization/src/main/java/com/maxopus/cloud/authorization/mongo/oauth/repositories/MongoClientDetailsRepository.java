package com.maxopus.cloud.authorization.mongo.oauth.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.maxopus.cloud.authorization.mongo.oauth.domain.MongoClientDetails;

public interface MongoClientDetailsRepository extends MongoRepository<MongoClientDetails, String>, MongoClientDetailsRepositoryBase {
}
