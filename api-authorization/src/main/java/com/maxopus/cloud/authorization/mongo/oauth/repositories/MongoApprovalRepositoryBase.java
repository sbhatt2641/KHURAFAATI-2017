package com.maxopus.cloud.authorization.mongo.oauth.repositories;

import java.util.Collection;
import java.util.List;

import org.joda.time.LocalDate;

import com.maxopus.cloud.authorization.mongo.oauth.domain.MongoApproval;

public interface MongoApprovalRepositoryBase {
    boolean updateOrCreate(Collection<MongoApproval> mongoApprovals);

    boolean updateExpiresAt(LocalDate now, MongoApproval mongoApproval);

    boolean deleteByUserIdAndClientIdAndScope(MongoApproval mongoApproval);

    List<MongoApproval> findByUserIdAndClientId(String userId, String clientId);
}
