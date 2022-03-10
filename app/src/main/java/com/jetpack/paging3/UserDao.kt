package com.jetpack.paging3

import androidx.paging.DataSource
import androidx.room.*

/**
 * user 对应的dao类
 */
@Dao
interface UserDao {
    /**
     * 插入user对象
     * @param user Array<out User> 一个user或者多个user
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)//有了就替换
    fun insertUserAll(vararg user: User)
    /**
     * 删除user对象
     */
    @Delete
    fun delete(vararg user: User)
    /**
     * 删除全部对象
     */
    @Query("delete from user")
    fun deleteAll()

    /**
     * 更新user对象
     */
    @Update
    fun update(vararg user: User)

    /**
     * 查询所有user对象
     */
    @Query("select * from user")
    fun queryAll():List<User>
    /**
     * 查询所有user对象
     */
    @Query("select * from user WHERE id < :startId+1  ORDER BY id DESC LIMIT  :size   OFFSET 0")
    fun queryRange(startId:Int,size:Int):List<User>
    /**
     * 查询最顶上的对象
     */
    @Query("select * from user  ORDER BY id DESC LIMIT 1")
    fun top():User

    /**
     * paging需要用到这种模式
     */
    @Query("select * from user")
    fun queryAllForPaging():DataSource.Factory<Int,User>

    /**
     * 根据用户名查询
     */
    @Query("select * from user where name = :name")
    fun queryByName(name:String):User
}