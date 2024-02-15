package com.example.usermanagementapp.models

import java.io.Serializable

data class Model(
    val id: Int,
    val name: String,
    val image: String,
    val mobileNumber: String,
    val date: String,
    val location: String,
) : Serializable