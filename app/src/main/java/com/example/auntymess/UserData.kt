package com.example.auntymess

data class UserData(
    val name: String="",
    val email: String="",
    val profileImage: String=""
){
    constructor():this("","","")
}
