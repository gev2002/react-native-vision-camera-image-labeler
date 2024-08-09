import Foundation
import VisionCamera
import MLKitVision
import MLKitImageLabeling



@objc(VisionCameraImageLabeler)
public class VisionCameraImageLabeler: FrameProcessorPlugin {
    public override init(proxy: VisionCameraProxyHolder, options: [AnyHashable: Any]! = [:]) {
        super.init(proxy: proxy, options: options)

        let minConfidence = options["minConfidence"] as? Double

            if minConfidence == nil {
                labelerOptions = ImageLabelerOptions()
            } else if minConfidence! > 0 && minConfidence! < 1.0 {
                labelerOptions.confidenceThreshold = NSNumber(value: minConfidence!)
            } else {
                labelerOptions = ImageLabelerOptions()
            }
    }
    private var labelerOptions = ImageLabelerOptions();
    public override func callback(
        _ frame: Frame,
        withArguments arguments: [AnyHashable: Any]?
    ) -> Any {
        var data:[Any] = []
        let buffer = frame.buffer
        let image = VisionImage(buffer: buffer);
        print(" FRAME   \(frame.orientation.rawValue)")
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
    func getOrientation(
      orientation: UIImage.Orientation
    ) -> UIImage.Orientation {
      switch orientation {
        case .up:
          return .up
        case .left:
          return .right
        case .down:
          return .down
        case .right:
          return .left
        default:
          return .up
      }
    }

}
