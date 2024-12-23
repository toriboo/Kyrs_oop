package com.example.Kyrs_oop.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

public class ResponseParser {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    public Map<String, ApiResponse.GroupData> parsing(String response) {
        ApiResponse apiResponse = gson.fromJson(response, ApiResponse.class);
        return apiResponse.getGroups();
    }
}
