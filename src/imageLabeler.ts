import {
    type FrameProcessorPlugin,
    VisionCameraProxy,
  } from 'react-native-vision-camera';
  import type {
    Frame,
    ImageLabelerPlugin,
    ImageLabelingOptions,
    Label,
  } from './types';
  import { Platform } from 'react-native';
  
  const LINKING_ERROR : string =
    `The package 'react-native-vision-camera-image-labeler' doesn't seem to be linked. Make sure: \n\n` +
    Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
    '- You rebuilt the app after installing the package\n' +
    '- You are not using Expo Go\n';
  
  export function createImageLabelerPlugin(
    options: ImageLabelingOptions
  ): ImageLabelerPlugin {
    const plugin: FrameProcessorPlugin | undefined =
      VisionCameraProxy.initFrameProcessorPlugin('labelerImage', {
        ...options,
      });
    if (!plugin) {
      throw new Error(LINKING_ERROR);
    }
    return {
      scanImage: (frame: Frame): Label => {
        'worklet';
        return plugin.call(frame) as unknown as Label;
      },
    };
  }
  