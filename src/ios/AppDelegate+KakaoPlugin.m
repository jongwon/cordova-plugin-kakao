//
//  AppDelegate+KakaoPlugin.m
//  cordova-test1
//
//  Created by JongWon Choi on 2018. 2. 1..
//

#import "AppDelegate.h"
#import <Foundation/Foundation.h>
#import <KakaoOpenSDK/KakaoOpenSDK.h>


@implementation AppDelegate (AppEvent)

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation {
    
    if ([KOSession isKakaoAccountLoginCallback:url]){return [KOSession handleOpenURL:url];}
    
}

- (void)applicationDidBecomeActive:(UIApplication *)application{[KOSession handleDidBecomeActive];}


@end
