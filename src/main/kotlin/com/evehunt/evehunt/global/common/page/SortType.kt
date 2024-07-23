package com.evehunt.evehunt.global.common.page

enum class SortType {
    CLOSE, NEW, TITLE, HOST, RANK;
    companion object
    {
        fun of(code: String): SortType {
            return try {
                SortType.valueOf(code.uppercase())
            } catch (e: IllegalArgumentException) {
                NEW
            }
        }
    }
}