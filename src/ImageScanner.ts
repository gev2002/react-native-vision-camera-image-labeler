import { NativeModules, Platform } from 'react-native';
import type { ImageScannerOptions, Label } from './types';

export async function ImageScanner(
  options: ImageScannerOptions
): Promise<Label> {
  const { ImageScannerModule } = NativeModules;
  const { uri, orientation, minConfidence } = options;
  if (!uri) {
    throw Error("Can't resolve img uri");
  }
  if (Platform.OS === 'ios') {
    return await ImageScannerModule.process(
      uri.replace('file://', ''),
      orientation || 'portrait',
      minConfidence || 1.0
    );
  } else {
    return await ImageScannerModule.process(uri, minConfidence || 1.0);
  }
}
