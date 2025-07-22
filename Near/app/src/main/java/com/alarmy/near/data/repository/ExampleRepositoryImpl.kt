package com.alarmy.near.data.repository

import javax.inject.Inject

class ExampleRepositoryImpl
    @Inject
    constructor() : ExampleRepository {
        override fun getExampleData(): String = "Hello from Repository"
    }
