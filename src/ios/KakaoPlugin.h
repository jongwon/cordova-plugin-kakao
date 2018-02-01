#import <Cordova/CDVPlugin.h>

@interface KakaoPlugin : CDVPlugin {
    
}

// The hooks for our plugin commands
- (void)login:(CDVInvokedUrlCommand *)command;
- (void)logout:(CDVInvokedUrlCommand *)command;
- (void)share:(CDVInvokedUrlCommand *)command;

@end
