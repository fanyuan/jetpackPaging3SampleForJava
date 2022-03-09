package com.jetpack.paging3;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserResponse {
    public static List<User> getUsers(int page,int pageSize){
        try {Thread.sleep(900);} catch (InterruptedException e) {e.printStackTrace();}
        Log.d("debug_log","UserResponse --- getUsers page = " + page + " pageSize = " + pageSize +" --- " + Thread.currentThread().getName());
        int size = page + pageSize;
        List<User> items = new ArrayList<>();
        for (;page<size;page++){
            items.add(getUser(page));
        }
        return items;
    }

    private static User getUser(int i) {
        return new User(i,"name" + i,i);
    }
}
