package com.example.myapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
    val studentId: String,
    val name: String,
    val phoneNumber: String,
    val address: String
) : Parcelable
