package com.upayment.upaymentsdk.track.charge

class PluginBuilder {
    // Properties that match the fields in Plugin class
    var src: String = ""

    fun build(): Plugin {
        return Plugin(
            src = src
        )
    }
}






