//
//  NSString-Encoding.h
//  QuotesTrivia
//
//  Created by Reilly Watson on 10-06-03.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface NSString (Encoding)

- (NSString *)stringByDecodingXMLEntities;

@end
