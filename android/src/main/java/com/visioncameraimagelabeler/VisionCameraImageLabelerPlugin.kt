package com.visioncameraimagelabeler

import android.media.Image
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.*
import com.mrousavy.camera.frameprocessors.*
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class VisionCameraImageLabelerPlugin(proxy: VisionCameraProxy, options: Map<String, Any>?) :
    FrameProcessorPlugin() {
    private var labeler: ImageLabeler
    private val minConfidence = options?.get("minConfidence").toString().toDoubleOrNull() ?: 0.0

    init {
        labeler = if (minConfidence in 0.1..1.0) {
            val imageLabelerOptions =
                ImageLabelerOptions.Builder().setConfidenceThreshold(minConfidence.toFloat())
                    .build()
            ImageLabeling.getClient(imageLabelerOptions)
        } else {
            ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        }
    }

    override fun callback(frame: Frame, arguments: Map<String, Any>?): Any {
        try {
            val mediaImage: Image = frame.image
            val image =
                InputImage.fromMediaImage(mediaImage, frame.imageProxy.imageInfo.rotationDegrees)
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
