package com.maxopus.cloud.authorization.mongo.oauth.repositories;

import com.maxopus.cloud.authorization.mongo.oauth.domain.MongoClientDetails;

public interface MongoClientDetailsRepositoryBase {
    boolean deleteByClientId(String clientId);

    boolean update(MongoClientDetails mongoClientDetails);

    boolean updateClientSecret(String clientId, String newSecret);

    MongoClientDetails findByClientId(String clientId) throws IllegalArgumentException;
}
