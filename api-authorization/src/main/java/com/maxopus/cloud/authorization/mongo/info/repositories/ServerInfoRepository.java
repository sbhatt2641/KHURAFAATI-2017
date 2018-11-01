package com.maxopus.cloud.authorization.mongo.info.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.maxopus.cloud.authorization.mongo.info.domain.ServerInfo;

public interface ServerInfoRepository extends MongoRepository<ServerInfo, String>, ServerInfoRepositoryBase {
}
