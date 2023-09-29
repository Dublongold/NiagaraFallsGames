package com.semdiniagara.yisofalls.help

import android.content.Intent
import android.os.Parcelable

class IntentBuilder {
    private val resultIntent = Intent()
    fun action(action: String): IntentBuilder {
        resultIntent.action = action
        return this
    }

    fun addCategory(category: String): IntentBuilder {
        resultIntent.addCategory(category)
        return this
    }

    fun<T> putExtra(key: String, value: T): IntentBuilder where T: Parcelable {
        resultIntent.putExtra(key, value)
        return this
    }

    fun putExtra(key: String, value: Array<Intent>): IntentBuilder {
        resultIntent.putExtra(key, value)
        return this
    }

    fun type(type: String): IntentBuilder {
        resultIntent.type = type
        return this
    }

    fun build() = resultIntent
}