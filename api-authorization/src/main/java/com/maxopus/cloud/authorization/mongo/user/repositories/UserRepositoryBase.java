package com.maxopus.cloud.authorization.mongo.user.repositories;

public interface UserRepositoryBase {

    boolean changePassword(String oldPassword, String newPassword, String username);

}
