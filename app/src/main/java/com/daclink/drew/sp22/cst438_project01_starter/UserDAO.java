package com.daclink.drew.sp22.cst438_project01_starter;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.Update;
import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    void insertUser(User user);
    @Update
    void updateUser(User user);
    @Delete
    void deleteUser(User user);
    @Query("SELECT * FROM user")
    List<User> listUsers();
    @Query("SELECT * FROM user WHERE username = :username")
    User findUser(String username);
}
