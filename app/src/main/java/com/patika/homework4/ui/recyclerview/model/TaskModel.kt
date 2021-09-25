package com.patika.homework4.ui.recyclerview.model

data class TaskModel(
    var completed: Boolean,
    var description: String,
    var _id: String?=null
)
