package com.maxopus.cloud.authorization.mongo.oauth.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.maxopus.cloud.authorization.mongo.oauth.domain.MongoApproval;

public interface MongoApprovalRepository extends MongoRepository<MongoApproval, String>, MongoApprovalRepositoryBase {
}
