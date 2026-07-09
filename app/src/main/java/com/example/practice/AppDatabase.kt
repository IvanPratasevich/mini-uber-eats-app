package com.example.practice

import android.content.Context
import androidx.room.*

@Database(entities = [Restaurant::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "restaurant_database"
                )
                .allowMainThreadQueries()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromCategory(category: RestaurantCategory): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(value: String): RestaurantCategory {
        return RestaurantCategory.valueOf(value)
    }
}
