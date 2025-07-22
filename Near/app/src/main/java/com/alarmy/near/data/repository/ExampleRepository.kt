package com.alarmy.near.data.repository

import kotlinx.coroutines.flow.Flow

interface ExampleRepository {
    fun getExampleData(): String

    fun getData(): Flow<String>
}
