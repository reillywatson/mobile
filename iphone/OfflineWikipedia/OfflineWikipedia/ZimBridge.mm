//
//  ZimBridge.m
//  OfflineWikipedia
//
//  Created by Reilly Watson on 2012-08-13.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import "ZimBridge.h"

#include <zim/file.hpp>
#include <zim/fileiterator.hpp>

static void tryIt() {
    try
    {
        zim::File file("/Users/reilly/Downloads/wiki.zim");
        
        zim::File::const_iterator it = file.findByTitle('A', "Wikipedia");
        if (it == file.end())
            NSLog(@"article not found");
        if (it->isRedirect())
            std::cout << "see: " << it->getRedirectArticle().getTitle() << std::endl;
        else
            std::cout << it->getData();
/*
        for (zim::File::const_iterator it = f.begin(); it != f.end(); ++it)
        {
            std::cout << "url: " << it->getUrl() << " title: " << it->getTitle() << '\n';
        }*/
    }
    catch (const std::exception& e)
    {
        std::cerr << e.what() << std::endl;
    }
}


@implementation ZimBridge

-(void) go {
    tryIt();
}

@end
