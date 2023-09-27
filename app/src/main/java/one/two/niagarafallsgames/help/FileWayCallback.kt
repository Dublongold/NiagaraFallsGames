package one.two.niagarafallsgames.help

import android.net.Uri
import android.webkit.ValueCallback

class FileWayCallback(private val fileWayCallback: ValueCallback<Array<Uri>>?) {
    fun onReceiveValue(value: Array<Uri>?) {
        fileWayCallback?.onReceiveValue(value)
    }

    fun default(wayCallback: Uri?) {
        if(wayCallback != null) {
            onReceiveValue(arrayOf(wayCallback))
        }
        else {
            onReceiveValue(null)
        }
    }
}