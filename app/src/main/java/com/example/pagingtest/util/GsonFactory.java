package com.example.pagingtest.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.util.Date;

public class GsonFactory {

    public static Gson createCommonGson() {
        return new GsonBuilder()
                .create();
    }
}
