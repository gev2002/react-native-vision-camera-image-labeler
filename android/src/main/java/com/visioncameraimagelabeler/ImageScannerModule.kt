package com.visioncameraimagelabeler

import android.net.Uri
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class ImageScannerModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    private lateinit var labeler: ImageLabeler

    @ReactMethod
    fun process(uri: String, minConfidence: Float, promise: Promise) {
        labeler = if (minConfidence in 0.1..1.0) {
            val imageLabelerOptions =
                ImageLabelerOptions.Builder().setConfidenceThreshold(minConfidence.toFloat())
                    .build()
            ImageLabeling.getClient(imageLabelerOptions)
        } else {
            ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        }

        val parsedUri = Uri.parse(uri)
        val data = WritableNativeArray()
        val image = InputImage.fromFilePath(this.reactApplicationContext, parsedUri)
        val task: Task<List<ImageLabel>> = labeler.process(image)
        try {
            val labels: List<ImageLabel> = Tasks.await(task)
            for (label in labels) {
                if (label.text.isEmpty()) {
                    promise.resolve(WritableNativeArray())
                }
                val obj = WritableNativeMap()
                obj.putString("labelText", label.text)
                obj.putDouble("confidence", label.confidence.toDouble())
                data.pushMap(obj)
            }
            promise.resolve(data)
        } catch (e: Exception) {
            e.printStackTrace()
            promise.reject("Error", "Error processing image scanner")
        }
    }

    override fun getName(): String {
        return NAME
    }

    companion object {
        const val NAME = "ImageScannerModule"
    }


}
