package com.alarmy.near.data.mapper

import com.alarmy.near.model.Example
import com.alarmy.near.network.response.ExampleEntity

fun ExampleEntity.toModel(): Example =
    Example(
        id = id.toString(),
    )
