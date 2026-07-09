package com.example.practice

import androidx.room.*

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurants")
    fun getAll(): List<Restaurant>

    @Query("SELECT * FROM restaurants WHERE id = :id")
    fun getById(id: Int): Restaurant?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(restaurant: Restaurant)

    @Update
    fun update(restaurant: Restaurant)

    @Delete
    fun delete(restaurant: Restaurant)

    @Query("DELETE FROM restaurants")
    fun deleteAll()
}
