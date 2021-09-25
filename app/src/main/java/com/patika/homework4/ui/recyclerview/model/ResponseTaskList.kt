package com.patika.homework4.ui.recyclerview.model

import com.google.gson.annotations.SerializedName

data class ResponseTaskList(
    @SerializedName("data")
    var taskList: MutableList<TaskModel>,
    var count:Int=0
)
