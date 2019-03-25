@file:JvmName("DebugExtensions")

package net.kibotu.android.recyclerviewpresenter.v2

import android.util.Log

internal val debug = false

internal fun Any.log(message: String?) {
    if (debug)
        Log.d(this::class.java.simpleName, "$message")
}