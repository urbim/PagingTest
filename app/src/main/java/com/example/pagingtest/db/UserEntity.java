package com.example.pagingtest.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.pagingtest.dto.UserDto;

@Entity(tableName = "users")
public class UserEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private Integer id;

    @ColumnInfo(name = "user_representation")
    private UserDto user;

    @ColumnInfo(name = "paging_prev_key")
    private Integer pagingPrevKey;

    @ColumnInfo(name = "paging_next_key")
    private Integer pagingNextKey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Integer getPagingPrevKey() {
        return pagingPrevKey;
    }

    public void setPagingPrevKey(Integer pagingPrevKey) {
        this.pagingPrevKey = pagingPrevKey;
    }

    public Integer getPagingNextKey() {
        return pagingNextKey;
    }

    public void setPagingNextKey(Integer pagingNextKey) {
        this.pagingNextKey = pagingNextKey;
    }
}
