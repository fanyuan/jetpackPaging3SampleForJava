package com.jetpack.paging3;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {User.class}, version = 1)
public abstract class UserDataBase extends RoomDatabase {
    private static final String name = "User";
    private static volatile UserDataBase instance;
    public abstract UserDao userDao();

    public static UserDataBase getInstance(Context context){
        if(instance == null){
            synchronized (UserDataBase.class){
                if (instance == null){
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            UserDataBase.class,
                            name)
                            //.addMigrations() 添加升级规则
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instance;
    }

}
