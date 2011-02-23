//
//  DatabaseManager.m
//  Movies
//
//  Created by Reilly Watson on 11-02-23.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "DatabaseManager.h"
#import <sqlite3.h>
#import "NominatedItem.h"
#import "Quote.h"

@interface DatabaseManager (Private)
-(void)checkAndCreateDatabase;
@end

@implementation DatabaseManager

static DatabaseManager *instance = NULL;

+(DatabaseManager *)sharedInstance {
	if (instance == NULL) {
		instance = [DatabaseManager new];
	}
	return instance;
}

-(id)init {
	self = [super init];
	[self checkAndCreateDatabase];
	return self;
}

-(void) checkAndCreateDatabase {
	NSString *databaseName = @"oscars.sqlite";
	// Get the path to the documents directory and append the databaseName
	NSArray *documentPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
	NSString *documentsDir = [documentPaths objectAtIndex:0];
	NSString *databasePath = [documentsDir stringByAppendingPathComponent:databaseName];
	// Check if the SQL database has already been saved to the users phone, if not then copy it over
	BOOL success;
	// Create a FileManager object, we will use this to check the status
	// of the database and to copy it over if required
	NSFileManager *fileManager = [NSFileManager defaultManager];
	// Check if the database has already been created in the users filesystem
	success = [fileManager fileExistsAtPath:databasePath];
	if(!success) {
		// If not then proceed to copy the database from the application to the users filesystem
		// Get the path to the database in the application package
		NSString *databasePathFromApp = [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:databaseName];
		// Copy the database from the package to the users filesystem
		[fileManager copyItemAtPath:databasePathFromApp toPath:databasePath error:nil];
	}
	[fileManager release];
	sqlite3_open([databasePath UTF8String], &_db);
}

-(void)dealloc {
	[super dealloc];
	sqlite3_close(_db);
}

typedef id(^PopulateFn)(sqlite3_stmt *compiledStatement);
-(NSArray *)populateFromDb:(NSString *)statement popFn:(PopulateFn)fn {
	NSMutableArray *results = [NSMutableArray new];
	sqlite3_stmt *compiledStatement;
	if (sqlite3_prepare_v2(_db, [statement UTF8String], -1, &compiledStatement, NULL) == SQLITE_OK) {
		while(sqlite3_step(compiledStatement) == SQLITE_ROW) {
			[results addObject:fn(compiledStatement)];
		}
	}
	sqlite3_finalize(compiledStatement);
	return results;
}

PopulateFn quotePop = ^id(sqlite3_stmt *compiledStatement) {
	Quote *quote = [[Quote new] autorelease];
	quote->quote = [[NSString stringWithUTF8String:(char *)sqlite3_column_text(compiledStatement, 0)] retain];
	quote->film = [[NSString stringWithUTF8String:(char *)sqlite3_column_text(compiledStatement, 1)] retain];
	return quote;
};

PopulateFn itemPop = ^id(sqlite3_stmt *compiledStatement) {
	NominatedItem *item = [[NominatedItem new] autorelease];
	item->awardName = [[NSString stringWithUTF8String:(char *)sqlite3_column_text(compiledStatement, 0)] retain];
	item->year = sqlite3_column_int(compiledStatement, 1);
	item->film = [[NSString stringWithUTF8String:(char *)sqlite3_column_text(compiledStatement, 2)] retain];
	item->nominee = [[NSString stringWithUTF8String:(char *)sqlite3_column_text(compiledStatement, 3)] retain];
	item->role = [[NSString stringWithUTF8String:(char *)sqlite3_column_text(compiledStatement, 4)] retain];
	item->isWinner = (sqlite3_column_int(compiledStatement, 5) != 0);
	item->actors = [[NSString stringWithUTF8String:(char *)sqlite3_column_text(compiledStatement, 6)] retain];
	return item;
};

-(NSArray *)quotesForFilm:(NSString *)film {
	// this is some pretty weak sql sanitization, but we don't allow the user to enter new films so I don't care!
	NSString *statement = [NSString stringWithFormat:@"SELECT Quote, Film FROM Quote WHERE Film='%@'",[film stringByReplacingOccurrencesOfString:@"'" withString:@"''"]];
	return [self populateFromDb:statement popFn:quotePop];
}

-(NSArray *)nominatedItemsFromStatement:(NSString *)statement {
	return [self populateFromDb:statement popFn:itemPop];
}

-(NSArray *) nomineesForYear:(int)year awardName:(NSString *)awardName {
	return [self nominatedItemsFromStatement:[NSString stringWithFormat:@"select awardname,year,film,nominee,role,iswinner,actors from oscar where year=%d and awardname='%@'", year, awardName]];	
}

-(NSArray *) nomineesForYear:(int)year {
	return [self nominatedItemsFromStatement:[NSString stringWithFormat:@"select awardname,year,film,nominee,role,iswinner,actors from oscar where year=%d", year]];
}

-(NSArray *)getYearRanges:(NSString *)awardname {
	return [[self populateFromDb:[NSString stringWithFormat:@"select min(year), max(year) from oscar where awardname=%@", awardname] popFn:^id(sqlite3_stmt *compiledStatement) {
		NSNumber *min = [NSNumber numberWithInt:sqlite3_column_int(compiledStatement, 0)];
		NSNumber *max = [NSNumber numberWithInt:sqlite3_column_int(compiledStatement, 1)];
		NSArray *pair = [NSArray arrayWithObjects:min,max,nil];
		return pair;
	}] objectAtIndex:0];
}

-(NSArray *)getBestSupportingActorEntries:(int) year {
	return [self nomineesForYear:year awardName:@"Best Supporting Actor"];
}
-(NSArray *)getBestActorEntries:(int)year {
	return [self nomineesForYear:year awardName:@"Best Actor"];
}
-(NSArray *)getBestSupportingActressEntries:(int) year {
	return [self nomineesForYear:year awardName:@"Best Supporting Actress"];
}
-(NSArray *)getBestActressEntries:(int)year {
	return [self nomineesForYear:year awardName:@"Best Actress"];
}
-(NSArray *)getBestPictureEntries:(int)year {
	return [self nomineesForYear:year awardName:@"Best Picture"];
}
-(NSArray *)getBestDirectorEntries:(int)year {
	return [self nomineesForYear:year awardName:@"Best Director"];
}

-(Quote *)getRandomQuote {
	NSArray *results = [self populateFromDb:@"select quote, film from quote order by random() limit 1" popFn:quotePop];
	return [results objectAtIndex:0];
}

@end
