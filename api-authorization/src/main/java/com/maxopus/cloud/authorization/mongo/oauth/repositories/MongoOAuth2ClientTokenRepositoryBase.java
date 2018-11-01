package com.maxopus.cloud.authorization.mongo.oauth.repositories;

import com.maxopus.cloud.authorization.mongo.oauth.domain.MongoOAuth2ClientToken;

public interface MongoOAuth2ClientTokenRepositoryBase {
    boolean deleteByAuthenticationId(String authenticationId);

    MongoOAuth2ClientToken findByAuthenticationId(String authenticationId);
}
