package com.visioncameraimagelabeler

import android.media.Image
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.mrousavy.camera.frameprocessors.Frame
import com.mrousavy.camera.frameprocessors.FrameProcessorPlugin
import com.mrousavy.camera.frameprocessors.VisionCameraProxy

class ImageLabelerModule (proxy : VisionCameraProxy, options: Map<String, Any>?): FrameProcessorPlugin() {
    private var labeler: ImageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
    private var minConfidence = options?.get("minConfidence").toString().toFloat()

    init {
        if (minConfidence < 1.0 && minConfidence > 0.0) {
            val labelerOptions = ImageLabelerOptions.Builder()
                .setConfidenceThreshold(minConfidence)
                .build()
            labeler = ImageLabeling.getClient(labelerOptions)
        }
    }
    override fun callback(frame: Frame, arguments: Map<String, Any>?): Any {
    try {
        val mediaImage: Image = frame.image
        val image = InputImage.fromMediaImage(mediaImage, frame.imageProxy.imageInfo.rotationDegrees)
        val task: Task<List<ImageLabel>> = labeler.process(image)
        val labels: List<ImageLabel> = Tasks.await(task)
        val array = WritableNativeArray()
        for (label in labels) {
          val map = WritableNativeMap()
          map.putString("label", label.text)
          map.putDouble("confidence", label.confidence.toDouble())
          array.pushMap(map)
        }
        return array.toArrayList()
      } catch (e: Exception) {
           throw Exception("Error processing image labeler: $e")
      }
  }
}
