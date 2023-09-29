package com.semdiniagara.yisofalls.help

import android.app.Activity
import android.os.Environment
import java.io.File

fun Activity.createTempImageFile(prefix: String): File {
    return File.createTempFile(
        prefix,
        getDefaultSuffix(),
        getExternalFilesDir(Environment.DIRECTORY_PICTURES))
}

fun getDefaultSuffix() = ".jpg"