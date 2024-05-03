#import <Foundation/Foundation.h>
#import <VisionCamera/FrameProcessorPlugin.h>
#import <VisionCamera/FrameProcessorPluginRegistry.h>
#import <VisionCamera/Frame.h>


#if __has_include("ImageLabeler/ImageLabeler-Swift.h")
#import "ImageLabeler/ImageLabeler-Swift.h"
#else
#import "ImageLabeler-Swift.h"
#endif

@interface ImageLabeler (FrameProcessorPluginLoader)
@end

@implementation ImageLabeler (FrameProcessorPluginLoader)
+ (void) load {
  [FrameProcessorPluginRegistry addFrameProcessorPlugin:@"labelerImage"
    withInitializer:^FrameProcessorPlugin*(VisionCameraProxyHolder* proxy, NSDictionary* options) {
    return [[ImageLabeler alloc] initWithProxy:proxy withOptions:options];
  }];
}
@end
