package com.yogas.api.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val login: String,
    val avatarUrl: String,
    val htmlUrl: String,
    val followersUrl: String,
    val followingUrl: String,
    val followers: Int,
    val following: Int,
    val reposUrl: String,
    val repos: Int
) : Parcelable
