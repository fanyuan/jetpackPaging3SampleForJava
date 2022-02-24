package com.jetpack.paging3;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserResponse {
    public static List<User> getUsers(int page,int pageSize){
        try {Thread.sleep(900);} catch (InterruptedException e) {e.printStackTrace();}
        Log.d("debug_log","UserResponse --- getUsers " + Thread.currentThread().getName());
        List<User> items = new ArrayList<>();
        for (int i=0 ;i<pageSize;i++){
            items.add(getUser(page+i));
        }
        return items;
    }

    private static User getUser(int i) {
        return new User("name" + i,i);
    }
}
