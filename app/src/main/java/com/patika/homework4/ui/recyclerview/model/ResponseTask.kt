package com.patika.homework4.ui.recyclerview.model

import com.google.gson.annotations.SerializedName

data class ResponseTask(
    @SerializedName("data")
    var task: TaskModel,
)
