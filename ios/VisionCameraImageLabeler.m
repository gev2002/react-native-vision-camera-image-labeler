#import <Foundation/Foundation.h>
#import <VisionCamera/FrameProcessorPlugin.h>
#import <VisionCamera/FrameProcessorPluginRegistry.h>
#import <VisionCamera/Frame.h>


#if __has_include("VisionCameraImageLabeler/VisionCameraImageLabeler-Swift.h")
#import "VisionCameraImageLabeler/VisionCameraImageLabeler-Swift.h"
#else
#import "VisionCameraImageLabeler-Swift.h"
#endif

@interface VisionCameraImageLabeler (FrameProcessorPluginLoader)
@end

@implementation VisionCameraImageLabeler (FrameProcessorPluginLoader)
+ (void) load {
  [FrameProcessorPluginRegistry addFrameProcessorPlugin:@"labelerImage"
    withInitializer:^FrameProcessorPlugin*(VisionCameraProxyHolder* proxy, NSDictionary* options) {
    return [[VisionCameraImageLabeler alloc] initWithProxy:proxy withOptions:options];
  }];
}
@end
