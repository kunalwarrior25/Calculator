package com.example.calculator_history.reco

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MainList::class], version = 1, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}
