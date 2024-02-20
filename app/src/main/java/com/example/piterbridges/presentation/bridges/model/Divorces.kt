package com.example.piterbridges.presentation.bridges.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Divorces(
    val startTime: String,
    val endTime: String,
) : Parcelable
