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
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class ImageScannerModule (reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    private var labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

     @ReactMethod
     fun process(uri:String,minConfidence:Float,promise: Promise){
         if (minConfidence < 1.0 && minConfidence > 0.0) {
             val options = ImageLabelerOptions.Builder()
                 .setConfidenceThreshold(minConfidence)
                 .build()
             labeler = ImageLabeling.getClient(options)
         }


         val parsedUri = Uri.parse(uri)
         val data = WritableNativeArray()
         val image = InputImage.fromFilePath(this.reactApplicationContext,parsedUri)
         val task: Task<List<ImageLabel>> = labeler.process(image)
         try {
             val labels: List<ImageLabel> = Tasks.await(task)
             for (label in labels) {
                 if (label.text.isEmpty()){
                     promise.resolve(WritableNativeArray())
                 }
                 val obj = WritableNativeMap()
                 obj.putString("labelText",label.text)
                 obj.putDouble("confidence", label.confidence.toDouble())
                 data.pushMap(obj)
             }
             promise.resolve(data)
         } catch (e: Exception) {
             e.printStackTrace()
             promise.reject("Error", "Error processing image")
         }



     }

    override fun getName(): String {
        return NAME
    }

    companion object {
        const val NAME = "ImageScannerModule"
    }


}