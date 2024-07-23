package com.evehunt.evehunt.global.common.page

enum class SearchType { TITLE, NONE, DESCRIPTION, TITLEDESCRIPTION, HOST, TAG, PARTICIPATE, EMAIL, ANSWER;
    companion object
    {
        fun of(code: String): SearchType {
            return try {
                println(code)
                SearchType.valueOf(code.uppercase())
            } catch (e: IllegalArgumentException) {
                NONE
            }
        }
    }
}