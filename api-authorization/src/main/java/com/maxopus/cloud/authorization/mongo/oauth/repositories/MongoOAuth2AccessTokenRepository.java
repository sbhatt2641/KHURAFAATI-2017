package com.maxopus.cloud.authorization.mongo.oauth.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.maxopus.cloud.authorization.mongo.oauth.domain.MongoOAuth2AccessToken;

@Repository
public interface MongoOAuth2AccessTokenRepository extends MongoRepository<MongoOAuth2AccessToken, String>, MongoOAuth2AccessTokenRepositoryBase {

}
