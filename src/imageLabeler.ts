import { VisionCameraProxy } from 'react-native-vision-camera';
import type {
  Frame,
  ImageLabelerPlugin,
  ImageLabelingOptions,
  Label,
} from './types';

const LINKING_ERROR = `Can't load plugin scanImage.Try cleaning cache or reinstall plugin.`;

export function createImageLabelerPlugin(
  options?: ImageLabelingOptions
): ImageLabelerPlugin {
  const plugin = VisionCameraProxy.initFrameProcessorPlugin('labelerImage', {
    ...options,
  });

  if (!plugin) {
    throw new Error(LINKING_ERROR);
  }
  return {
    scanImage: (frame: Frame): Label[] => {
      'worklet';
      // @ts-ignore
      return plugin.call(frame) as Label[];
    },
  };
}
