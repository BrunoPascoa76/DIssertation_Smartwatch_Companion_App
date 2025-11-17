package cm.project.dissertationsmartwatchcompanionapp.presentation

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

object KafkaHelper {
    private var client:OkHttpClient?=null
    private var brokerURL: String=""

    fun initProducer(brokerURL: String){
        this.brokerURL=brokerURL
        this.client=OkHttpClient()
    }

    fun sendReading(readingType: String, value: Int){
        val timestamp=System.currentTimeMillis() *1000 //since we can't get the same measure in nanoseconds (only elapsed)
        val json="""{
            | "reading_type": "$readingType",
            | "timestamp_ns": $timestamp,
            | "value": $value
            |}""".trimMargin()

        val requestBody=json.toRequestBody("application/vnd.kafka.json.v2+json".toMediaTypeOrNull())

        val request= Request.Builder()
            .url("$brokerURL/topics/smartwatch_data")
            .post(requestBody)
            .build()

        client?.newCall(request)?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Request failed: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.d("KAFKA-REST","Response failed: ${response.code}")
                    } else {
                        Log.d("KAFKA-REST","Response success: ${response.body?.string()}")
                    }
                }
            }
        })

    }

    fun close(){} //just to keep the same interface
}