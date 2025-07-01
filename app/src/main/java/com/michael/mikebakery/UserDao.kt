package com.michael.mikebakery

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun registerUser(user: User)

    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password LIMIT 1")
    suspend fun loginUser(username: String, password: String): User?

    @Query("SELECT * FROM user_table WHERE username = :username LIMIT 1")
    suspend fun findUserByUsername(username: String): User?
}