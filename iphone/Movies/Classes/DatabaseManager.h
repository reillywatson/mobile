//
//  DatabaseManager.h
//  Movies
//
//  Created by Reilly Watson on 11-02-23.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <sqlite3.h>
#import "Quote.h"

@interface DatabaseManager : NSObject {
	sqlite3 *_db;
}

+(DatabaseManager *)sharedInstance;

-(NSArray *)quotesForFilm:(NSString *)film;
-(NSArray *) nomineesForYear:(int)year awardName:(NSString *)awardName;
-(NSArray *) nomineesForYear:(int)year;
-(NSArray *)getYearRanges:(NSString *)awardname;
-(NSArray *)getBestSupportingActorEntries:(int) year;
-(NSArray *)getBestActorEntries:(int)year;
-(NSArray *)getBestSupportingActressEntries:(int) year;
-(NSArray *)getBestActressEntries:(int)year;
-(NSArray *)getBestPictureEntries:(int)year;
-(NSArray *)getBestDirectorEntries:(int)year;
-(Quote *)getRandomQuote;
-(NSArray *)getMoviesFromSameYear:(NSString *)film;

@end
