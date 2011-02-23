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

-(NSArray *)quotesForFilm:(NSString *)film {
	// this is some pretty weak sql sanitization, but we don't allow the user to enter new films so I don't care!
	NSString *statement = [NSString stringWithFormat:@"SELECT Quote FROM Quote WHERE Film='%@'",[film stringByReplacingOccurrencesOfString:@"'" withString:@"''"]];
	NSMutableArray *results = [NSMutableArray new];
	sqlite3_stmt *compiledStatement;
	if (sqlite3_prepare_v2(_db, [statement UTF8String], -1, &compiledStatement, NULL) == SQLITE_OK) {
		while(sqlite3_step(compiledStatement) == SQLITE_ROW) {
			Quote *quote = [[Quote new] autorelease];
			quote->film = [film retain];
			quote->quote = [[NSString stringWithUTF8String:(char *)sqlite3_column_text(compiledStatement, 1)] retain];
			[results addObject:quote];
		}
	}
	sqlite3_finalize(compiledStatement);
	return results;
	
}

-(NSArray *)nominatedItemsFromStatement:(NSString *)statement {
	NSMutableArray *results = [NSMutableArray new];
	sqlite3_stmt *compiledStatement;
	if (sqlite3_prepare_v2(_db, [statement UTF8String], -1, &compiledStatement, NULL) == SQLITE_OK) {
		while(sqlite3_step(compiledStatement) == SQLITE_ROW) {
			NominatedItem *item = [[NominatedItem new] autorelease];
			item->awardName = [[NSString stringWithUTF8String:(char *)sqlite3_column_text(compiledStatement, 1)] retain];
			item->year = sqlite3_column_int(compiledStatement, 2);
			item->film = [[NSString stringWithUTF8String:(char *)sqlite3_column_text(compiledStatement, 3)] retain];
			item->nominee = [[NSString stringWithUTF8String:(char *)sqlite3_column_text(compiledStatement, 4)] retain];
			item->role = [[NSString stringWithUTF8String:(char *)sqlite3_column_text(compiledStatement, 5)] retain];
			item->isWinner = (sqlite3_column_int(compiledStatement, 6) != 0);
			item->actors = [[NSString stringWithUTF8String:(char *)sqlite3_column_text(compiledStatement, 7)] retain];
			[results addObject:item];
		}
	}
	sqlite3_finalize(compiledStatement);
	return results;
}

-(NSArray *) nomineesForYear:(int)year {
	return [self nominatedItemsFromStatement:[NSString stringWithFormat:@"select awardname,year,film,nominee,role,iswinner,actors from oscar where year=%d", year]];
}

/*
public Integer[] getYearRanges(QuestionType type) {
	Integer[] result = new Integer[2];
	
	String award = getAwardNameFromQuestionType(type);
	Cursor cursor = theDataBase.query(
									  "Oscar"
									  , new String[]{"AwardName", "Year"}					// Columns
									  , "AwardName=?"										// Selection (where)
									  , new String[]{award}								// Selection args
									  , null												// Group By
									  , null												// Having
									  , "Year asc"										// Order By
									  , null);											// Limit
	
	if (cursor.moveToFirst()) {
		result[0] = cursor.getInt(1);
	}
	
	if (cursor.moveToLast()) {
		result[1] = cursor.getInt(1);
	}
	cursor.close();
	
	Log.d("--------------", award + result[0]+ " " + result[1]);
	return result;
}

private String getAwardNameFromQuestionType(QuestionType type) {
	String awardName = null;
	
	switch(type) {
		case ACTOR:
			awardName = BEST_ACTOR;
			break;
		case ACTRESS:
			awardName = BEST_ACTRESS;
			break;
		case DIRECTOR:
			awardName = BEST_DIRECTOR;
			break;
		case MOVIE:
			awardName = BEST_PICTURE;
			break;
		case SUPPORTING_ACTOR:
			awardName = BEST_SUPPORTING_ACTOR;
			break;
		case SUPPORTING_ACTRESS:
			awardName = BEST_SUPPORTING_ACTRESS;
			break;
		default:
			break;
	}
	
	return awardName;
}

public List<Actor> getBestSupportingActorEntries(int randomYear) {
	List<Actor> result = new ArrayList<Actor>();
	result = getActorFields(BEST_SUPPORTING_ACTOR, randomYear);
	return result;
}

public List<Actor> getBestSupportingActressesEntries(int randomYear) {
	List<Actor> result = new ArrayList<Actor>();
	result = getActorFields(BEST_SUPPORTING_ACTRESS, randomYear);
	return result;
}

public List<Actor> getBestActressEntries(int year) {
	List<Actor> result = new ArrayList<Actor>();
	result = getActorFields(BEST_ACTRESS, year);
	return result;
}

public List<Actor> getBestActorEntries(int year) {
	List<Actor> result = new ArrayList<Actor>();
	result = getActorFields(BEST_ACTOR, year);
	return result;
}

public List<NominatedPerson> getBestDirectorEntries(int randomYear) {
	List<NominatedPerson> result = new ArrayList<NominatedPerson>();
	
	String award = BEST_DIRECTOR;
	String year = String.valueOf(randomYear);
	Cursor cursor = theDataBase.query(
									  "Oscar"
									  , new String[]{"AwardName", "Year", "Film", "Nominee", "IsWinner" }	// Columns
									  , "AwardName=? AND Year=?"								// Selection (where)
									  , new String[]{award, year}								// Selection args
									  , null													// Group By
									  , null													// Having
									  , null													// Order By
									  , null);												// Limit
	
	while (cursor.moveToNext()) {
		NominatedPerson person = new NominatedPerson( 
													 award					 // award
													 , randomYear 			 // year
													 , cursor.getString(2)	 // film
													 , cursor.getString(3)	 // nominee
													 ,(cursor.getInt(4) == 1) // isWinner
													 );
		result.add(person);
	}
	cursor.close();
	return result;
}

public List<Movie> getBestPictureEntries(int yr) {
	List<Movie> result = new ArrayList<Movie>();
	
	String award = BEST_PICTURE;
	String year = String.valueOf(yr);
	Cursor cursor = theDataBase.query(
									  "Oscar"
									  , new String[]{"AwardName", "Year", "Film", "IsWinner", "Actors" }	// Columns
									  , "AwardName=? AND Year=?"										// Selection (where)
									  , new String[]{award, year}								// Selection args
									  , null													// Group By
									  , null													// Having
									  , null													// Order By
									  , null);												// Limit
	
	while (cursor.moveToNext()) {
		Movie movie = new Movie( 
								award					 // award
								, yr		 			 // year
								, cursor.getString(2)	 // film
								,(cursor.getInt(3) == 1) // isWinner
								, cursor.getString(4)	 // actors
								);
		result.add(movie);
	}
	cursor.close();
	return result;
}

private List<Actor> getActorFields(String award, int yr) {
	List<Actor> result = new ArrayList<Actor>();
	
	String year = String.valueOf(yr);
	Cursor cursor = theDataBase.query(
									  "Oscar"
									  , new String[]{"AwardName", "Year", "Film", "Nominee", "Role", "IsWinner" }	// Columns
									  , "AwardName=? AND Year=?"						// Selection (where)
									  , new String[]{award, year}								// Selection args
									  , null													// Group By
									  , null													// Having
									  , null													// Order By
									  , null);												// Limit
	
	while (cursor.moveToNext()) {
		Actor actor = new Actor( 
								award					 // award
								, yr		 			 // year
								, cursor.getString(2)	 // film
								, cursor.getString(3)	 // name
								, cursor.getString(4)	 // role
								,(cursor.getInt(5) == 1) // isWinner
								);
		result.add(actor);
	}
	cursor.close();
	
	return result;
}

public Quote getRandomQuote() {
	Quote result = null;
	
	Cursor cursor = theDataBase.query(
									  "Quote"
									  , new String[]{"Film", "Quote"}		// Columns
									  , null								// Selection (where)
									  , new String[]{}					// Selection args
									  , null								// Group By
									  , null								// Having
									  , "RANDOM()"						// Order By
									  , "1");								// Limit
	
	if (cursor.moveToFirst()) {
		result = new Quote(cursor.getString(0), cursor.getString(1));
	} 
	cursor.close();
	
	return result;
}

public List<Movie> getMoviesFromSameYear(String filmName) {
	List<Movie> result = new ArrayList<Movie>();
	
	Cursor cursor = theDataBase.rawQuery(
										 "SELECT AwardName, Year, Film, IsWinner, Actors " +
										 "FROM Oscar " +
										 "WHERE year IN ( SELECT year FROM oscar WHERE film LIKE ?)" +
										 "	AND awardName like 'Best Picture'" , new String[]{filmName});
	
	while (cursor.moveToNext()) {
		Movie movie = new Movie( 
								cursor.getString(0)		// award
								, cursor.getInt(1)		// year
								, cursor.getString(2)	// film
								,(cursor.getInt(3) == 1)// isWinner
								, cursor.getString(4)	// actors
								);
		result.add(movie);
	}
	cursor.close();
	
	return result;
}*/


@end
