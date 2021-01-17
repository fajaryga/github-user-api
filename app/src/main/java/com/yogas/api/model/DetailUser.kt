package com.yogas.api.model

class DetailUser(
    var id: Int = 0,
    var login: String = "",
    var avatar_url: String = "",
    var html_url: String = "",
    var name: String? = "",
    var followers: Int? = 0,
    var following: Int? = 0,
)