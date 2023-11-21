package com.upayment.upaymentsdk.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferenceHelper {

        val customerUniqueToken = "customer_unique_token"
        val USER_PASSWORD = "PASSWORD"

        @JvmStatic
        fun customPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

        inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
            val editMe = edit()
            operation(editMe)
            editMe.apply()
        }

        var SharedPreferences.customerToken
            get() = getString(customerUniqueToken, "")
            set(value) {
                editMe {
                    it.putString(customerUniqueToken, value)
                }
            }

        var SharedPreferences.password
            get() = getString(USER_PASSWORD, "")
            set(value) {
                editMe {
                    it.putString(USER_PASSWORD, value)
                }
            }

        var SharedPreferences.clearValues
            get() = { }
            set(value) {
                editMe {
                    it.clear()
                }
            }

}