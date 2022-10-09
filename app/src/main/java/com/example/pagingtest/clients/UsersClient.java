package com.example.pagingtest.clients;

import com.example.pagingtest.dto.UserDto;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsersClient {

    @GET("users")
    Single<List<UserDto>> getUsers(@Query("_start") Integer offset, @Query("_limit") Integer limit);
}
