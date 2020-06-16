package com.android.indicatorview

class PagesLessException : Exception() {
    override val message: String
        get() = "Pages must equal or larger than 2"
}