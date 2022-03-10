package com.jetpack.paging3;
/**
 * user 实体类定义
 *  indices = [Index(value = ["name"], unique = true)]  = name 唯一约束
 */

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

//@Entity(indices = {@Index(value = {"id","name"},unique = true)})
@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    private String name;
    @ColumnInfo(name = "_age")
    private int age;
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    @Ignore
    public User(int id,String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
