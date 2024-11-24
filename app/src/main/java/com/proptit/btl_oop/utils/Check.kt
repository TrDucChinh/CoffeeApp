package com.proptit.btl_oop.utils

object Check {
     fun isPasswordValid(password: String): Boolean {
        val passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}".toRegex()
        return password.matches(passwordPattern)
    }
}