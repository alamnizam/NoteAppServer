package com.codeturtle.data.model.note

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id:Int,
    val noteTitle:String,
    val description:String,
    val date:Long
)
