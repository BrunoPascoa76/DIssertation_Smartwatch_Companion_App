package cm.project.dissertationsmartwatchcompanionapp.presentation

import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.Properties

object KafkaRESTHelper {
    fun sendReading(brokerURL:String,readingType: String, value: Int){
        val timestamp=System.currentTimeMillis() *1000 //since we can't get the same measure in nanoseconds (only elapsed)
        val json="""{
            | "reading_type": "$readingType",
            | "timestamp_ns": $timestamp,
            | "value": $value
            |}""".trimMargin()

        val request_body=json.toRequestBody("application/vnd.kafka.json.v2+json".toMediaTypeOrNull())

        val request= Request.Builder()
            .url(brokerURL)
            .post(request_body)
            .build()

    }

    fun close(){

    }
}