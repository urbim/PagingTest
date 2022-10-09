package com.example.pagingtest.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        entities = {
                UserEntity.class
        },
        version = 1,
        exportSchema = false
)
@TypeConverters({
        UserDtoConverter.class
})
public abstract class ExampleDb extends RoomDatabase {

        private static ExampleDb INSTANCE;

        public static ExampleDb getInstance(Context context) {

                if (INSTANCE == null) {
                        initInstance(context);
                }

                return INSTANCE;
        }

        private static synchronized void initInstance(Context context) {

                if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                ExampleDb.class,
                                "example.db"
                        ).build();
                }
        }

        public abstract UserDao userDao();
}
