package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String password, String forgetToken);

    ServerResponse<String> resetPassword(String oldPassword, String newPassword, User user);

    ServerResponse<User> updateUserInfo( User user);

    ServerResponse<User> getUserInfo(Integer userId);

    ServerResponse checkAdmin(User user);
}
