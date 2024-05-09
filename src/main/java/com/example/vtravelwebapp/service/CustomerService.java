package com.example.vtravelwebapp.service;

import com.example.vtravelwebapp.model.UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerService {

    private final String backendUrl = "http://localhost:8080/api"; // Backend API URL

    public UserInfo fetchUserInfo(Object sessionToken) {
        String url = backendUrl + "/customers/user";

        // Make an HTTP GET request to fetch user information
        RestTemplate restTemplate = new RestTemplate();
        UserInfo userInfo = restTemplate.getForObject(url, UserInfo.class);

        return userInfo;
    }
}
