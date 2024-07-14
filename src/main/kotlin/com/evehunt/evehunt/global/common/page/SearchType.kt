package com.evehunt.evehunt.global.common.page

enum class SearchType {
    TITLE, DESCRIPTION, TITLEDESCRIPTION, HOST, TAG, PARTICIPATE;

    companion object
    {
        fun of(code: String): SearchType {
            return try {
                SearchType.valueOf(code.uppercase())
            } catch (e: IllegalArgumentException) {
                TITLE
            }
        }
    }
}