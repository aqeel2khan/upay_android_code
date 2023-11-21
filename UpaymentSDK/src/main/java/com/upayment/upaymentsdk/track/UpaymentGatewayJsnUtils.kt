package com.upayment.upaymentsdk.track

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Adil on 10/03/2015.
 */
 class UpaymentGatewayJsnUtils {
    companion object{

        @JvmStatic
        fun put(json: JSONArray, key: String?, value: String?) {
            try {
                json.put( value)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        @JvmStatic
        fun put(json: JSONObject, key: String?, value: String?) {
            try {
                json.put(key, value)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        @JvmStatic
        fun put(json: JSONObject, key: String?, value: Int) {
            try {
                json.put(key, value)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun put(json: JSONObject, key: String?, `object`: Any?) {
            try {
                json.put(key, `object`)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun merge(vararg json: JSONObject): JSONObject {
            val merged = JSONObject()
            for (obj in json) {
                val it: Iterator<*> = obj.keys()
                while (it.hasNext()) {
                    val key = it.next() as String
                    try {
                        merged.put(key, obj[key])
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
            return merged
        }
    }

}