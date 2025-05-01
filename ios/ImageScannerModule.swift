import Foundation
import UIKit
import React
import MLKitVision
import MLKitImageLabeling

@objc(ImageScannerModule)
class ImageScannerModule: NSObject {

    @objc(process:orientation:minConfidence:withResolver:withRejecter:)
    private func process(uri: String, orientation: String, minConfidence: Double, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        var data: [Any] = []
        let image = UIImage(contentsOfFile: uri)
        let options = ImageLabelerOptions()
        options.confidenceThreshold = NSNumber(value: minConfidence)
        let labeler = ImageLabeler.imageLabeler(options: options)

        if let image = image {
            do {
                let visionImage = VisionImage(image: image)
                visionImage.orientation = getOrientation(orientation: orientation)
                let labels = try labeler.results(in: visionImage)

                for label in labels {
                    var obj: [String: Any] = [:]
                    obj["labelText"] = label.text
                    obj["confidence"] = label.confidence
                    data.append(obj)
                }

                resolve(data)
            } catch {
                reject("Error", "Processing Image", nil)
            }
        } else {
            reject("Error", "Can't Find Photo", nil)
        }
    }

        private func getOrientation(
          orientation: String
        ) -> UIImage.Orientation {
            switch orientation {
            case "portrait":
                return .right
            case "landscapeLeft":
                return .up
            case "portraitUpsideDown":
                return .left
            case "landscapeRight":
                return  .down
            default:
                return .up
            }

        }
}
