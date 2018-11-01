package com.maxopus.cloud.authorization.mongo.oauth.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.maxopus.cloud.authorization.mongo.oauth.domain.MongoOAuth2RefreshToken;

@Repository
public interface MongoOAuth2RefreshTokenRepository extends MongoRepository<MongoOAuth2RefreshToken, String>, MongoOAuth2RefreshTokenRepositoryBase {
}
