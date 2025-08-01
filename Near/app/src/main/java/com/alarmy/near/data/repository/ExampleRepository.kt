package com.alarmy.near.data.repository

import com.alarmy.near.model.Example
import kotlinx.coroutines.flow.Flow

interface ExampleRepository {
    fun getExampleData(): String

    fun getData(): Flow<Example>
}
