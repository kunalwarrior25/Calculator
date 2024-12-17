package com.example.calculator_history.reco

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mainlist")
data class MainList(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val list: String,
    val value: String,
    val result: String
)

