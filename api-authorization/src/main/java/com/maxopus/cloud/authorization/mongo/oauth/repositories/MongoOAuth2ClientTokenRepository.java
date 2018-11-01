package com.maxopus.cloud.authorization.mongo.oauth.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.maxopus.cloud.authorization.mongo.oauth.domain.MongoOAuth2ClientToken;

public interface MongoOAuth2ClientTokenRepository extends MongoRepository<MongoOAuth2ClientToken, String>, MongoOAuth2ClientTokenRepositoryBase {
}
