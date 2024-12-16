package com.example.calculator_history.reco

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert
    suspend fun insertData(mainList: MainList)

    @Update
    suspend fun updateData(mainList: MainList)

    @Delete
    suspend fun deleteData(mainList: MainList)

    @Query("SELECT * FROM mainlist")
    fun getContact(): Flow<List<MainList>> // Get data as a Flow for real-time updates
}

