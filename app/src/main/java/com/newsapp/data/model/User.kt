package com.newsapp.data.model

data class User (
    var displayName: String = "",
    var email: String = "",
    var photoUrl: String = ""
) {
    override fun toString(): String {
        return "User(displayName='$displayName', email='$email', photoUrl='$photoUrl')"
    }
}