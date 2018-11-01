package com.maxopus.cloud.authorization.mongo.oauth.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.maxopus.cloud.authorization.mongo.oauth.domain.MongoOAuth2AccessToken;

public interface MongoOAuth2AccessTokenRepository extends MongoRepository<MongoOAuth2AccessToken, String>, MongoOAuth2AccessTokenRepositoryBase {

}
