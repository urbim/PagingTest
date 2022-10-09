package com.example.pagingtest.db;

import androidx.room.TypeConverter;

import com.example.pagingtest.dto.UserDto;
import com.example.pagingtest.util.GsonFactory;
import com.google.gson.Gson;

public class UserDtoConverter {

    private final Gson gson;

    public UserDtoConverter() {
        this.gson = GsonFactory.createCommonGson();
    }

    @TypeConverter
    public UserDto toUserDto(String json) {

        if (json == null) {
            return null;
        }

        return this.gson.fromJson(json, UserDto.class);
    }

    @TypeConverter
    public String toUserDtoJson(UserDto userDto) {

        if (userDto == null) {
            return null;
        }

        return this.gson.toJson(userDto);
    }
}
