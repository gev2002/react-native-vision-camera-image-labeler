import Foundation
import VisionCamera
import MLKitVision
import MLKitImageLabeling



@objc(VisionCameraImageLabeler)
public class VisionCameraImageLabeler: FrameProcessorPlugin {
    public override init(proxy: VisionCameraProxyHolder, options: [AnyHashable: Any]! = [:]) {
        super.init(proxy: proxy, options: options)
        let minConfidence = options["minConfidence"]!
        if let minConfidence = minConfidence as? Double, minConfidence > 0 {
            labelerOptions.confidenceThreshold = (minConfidence) as NSNumber
        }
    }
    private let labelerOptions = ImageLabelerOptions();
    public override func callback(
        _ frame: Frame,
        withArguments arguments: [AnyHashable: Any]?
    ) -> Any {
        var data:[Any] = []
        let buffer = frame.buffer
        let image = VisionImage(buffer: buffer);
        image.orientation = getOrientation(orientation: frame.orientation)
        let labeler = ImageLabeler.imageLabeler(options: labelerOptions);
        let dispatchGroup = DispatchGroup()
        dispatchGroup.enter()
        labeler.process(image) { labels, error in
            defer {
                dispatchGroup.leave()
            }
            guard error == nil, let labels = labels else { return }
            for label in labels {
            var obj : [String:Any] = [:]
            let labelText = label.text
            let confidence = label.confidence
                obj["label"] = labelText
                obj["confidence"] = confidence
                data.append(obj)
            }
        }
        dispatchGroup.wait()

        return data
    }
    private func getOrientation(orientation: UIImage.Orientation) -> UIImage.Orientation {
            switch orientation.rawValue {
            case 0: return .right
            case 1: return .left
            case 2: return .down
            case 3: return .up
            default: return .up
            }
    }

}
