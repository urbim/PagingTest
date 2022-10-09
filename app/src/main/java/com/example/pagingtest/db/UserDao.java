package com.example.pagingtest.db;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UserEntity> users);

    @Query("SELECT * FROM users ORDER BY id ASC")
    PagingSource<Integer, UserEntity> selectAll();

    @Query("DELETE FROM users")
    void clearAll();
}
