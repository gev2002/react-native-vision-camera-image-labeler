export type {
  Frame,
  ReadonlyFrameProcessor,
  FrameProcessorPlugin,
  FrameInternal,
  CameraProps,
  CameraDevice,
} from 'react-native-vision-camera';
export type { ForwardedRef } from 'react';
import type { CameraProps, Frame } from 'react-native-vision-camera';
export interface ImageLabelingOptions {
  minConfidence?:
    | 0.1
    | 0.01
    | 0.11
    | 0.2
    | 0.02
    | 0.22
    | 0.3
    | 0.03
    | 0.33
    | 0.4
    | 0.04
    | 0.44
    | 0.5
    | 0.05
    | 0.55
    | 0.6
    | 0.06
    | 0.66
    | 0.7
    | 0.07
    | 0.77
    | 0.8
    | 0.08
    | 0.88
    | 0.9
    | 0.09
    | 0.99
    | 1.0;
}

export type CameraTypes = {
  callback: Function;
  options?: ImageLabelingOptions;
} & CameraProps;

export interface Label {
  [key: string | number]: {
    confidence: number;
    label: string;
  };
}

export type ImageLabelerPlugin = {
  scanImage: (frame: Frame) => Label;
};

export type ImageScannerOptions = {
  uri: string;
  orientation?:
    | 'landscapeRight'
    | 'portrait'
    | 'portraitUpsideDown'
    | 'landscapeLeft';
  minConfidence: ImageLabelingOptions['minConfidence'];
};
