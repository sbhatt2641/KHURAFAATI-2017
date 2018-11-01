package com.maxopus.cloud.authorization.mongo.info.repositories;

import java.util.List;

import com.maxopus.cloud.authorization.mongo.info.domain.ServerInfo;

public interface ServerInfoRepositoryBase {

	ServerInfo findByUniqueKeys(String host, String port);
	List<ServerInfo> findByAppName(String appName);

}
