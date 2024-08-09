package com.visioncameraimagelabeler

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.facebook.react.bridge.NativeModule
import com.mrousavy.camera.frameprocessors.FrameProcessorPluginRegistry


class VisionCameraImageLabelerPackage : ReactPackage {
    companion object {
        init {
            FrameProcessorPluginRegistry.addFrameProcessorPlugin("labelerImage") { proxy, options ->
                VisionCameraImageLabelerPlugin(proxy, options)
            }
        }
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }

    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(
            ImageScannerModule(reactContext)
        )
    }
}
