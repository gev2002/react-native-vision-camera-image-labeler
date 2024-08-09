import React, { forwardRef, useMemo } from 'react';
import {
  Camera as VisionCamera,
  useFrameProcessor,
} from 'react-native-vision-camera';
import { createImageLabelerPlugin } from './imageLabeler';
import type {
  ReadonlyFrameProcessor,
  Frame,
  CameraTypes,
  ForwardedRef,
  Label,
  ImageLabelingOptions,
  ImageLabelerPlugin,
} from './types';
import { useRunOnJS } from 'react-native-worklets-core';

export const Camera = forwardRef(function Camera(
  props: CameraTypes,
  ref: ForwardedRef<any>
) {
  const { device, callback, options = {}, ...p } = props;
  const { scanImage } = useImageLabeler(options);
  const useWorklets = useRunOnJS(
    (data: Label[]): void => {
      callback(data);
    },
    [options]
  );
  const frameProcessor: ReadonlyFrameProcessor = useFrameProcessor(
    (frame: Frame) => {
      'worklet';
      const data: Label[] = scanImage(frame);
      // @ts-ignore
      // eslint-disable-next-line react-hooks/rules-of-hooks
      useWorklets(data);
    },
    []
  );
  return (
    <>
      {!!device && (
        <VisionCamera
          pixelFormat="yuv"
          ref={ref}
          frameProcessor={frameProcessor}
          device={device}
          {...p}
        />
      )}
    </>
  );
});

export function useImageLabeler(
  options?: ImageLabelingOptions
): ImageLabelerPlugin {
  return useMemo(() => createImageLabelerPlugin(options), [options]);
}
