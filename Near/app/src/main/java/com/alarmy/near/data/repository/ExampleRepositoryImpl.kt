package com.alarmy.near.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExampleRepositoryImpl
    @Inject
    constructor() : ExampleRepository {
        override fun getExampleData(): String = "Hello from Repository"

        override fun getData(): Flow<String> =
            flow {
                // val result = exampleService.fetchExample()
                // emit(result.data)
            }
    }
