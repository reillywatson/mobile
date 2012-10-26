//
//  BoardView.m
//  Letterpress Solver
//
//  Created by Reilly Watson on 2012-10-25.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import "BoardView.h"
#import "ManualEntryViewController.h"

@implementation BoardView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

-(void)fillRect:(CGRect)rect withColor:(UIColor*)color {
    CGContextRef ctx = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(ctx, [color CGColor]);
    CGContextFillRect(ctx, rect);
}

-(void)strokeRect:(CGRect)rect withColor:(UIColor*)color {
    CGContextRef ctx = UIGraphicsGetCurrentContext();
    CGContextSetStrokeColorWithColor(ctx, [color CGColor]);
    CGContextSetFillColorWithColor(ctx, [color CGColor]);
    CGContextStrokeRect(ctx, rect);
}

-(void)drawChar:(char)c inRect:(CGRect)rect {
    CGContextRef ctx = UIGraphicsGetCurrentContext();
    CGContextSetFontSize(ctx, rect.size.width);
    CGContextShowTextAtPoint(ctx, rect.origin.x + rect.size.width/2, rect.origin.y + rect.size.height/2, &c, 1);
}


// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    if (_board == nil) {
        return;
    }
    int cellWidth = [self frame].size.width / 5;
    int cellHeight = cellWidth;//[self frame].size.height / 5;
    int segmentWidth = self.frame.size.width / 3;
    int segmentHeight = self.frame.size.height - (cellHeight * 5) - 60;
    CGRect redRegion = CGRectMake(0, cellHeight*5 + 30, segmentWidth, segmentHeight);
    [self fillRect:redRegion withColor:[UIColor redColor]];
    CGRect grayRegion = CGRectMake(segmentWidth, cellHeight*5 + 30, segmentWidth, segmentHeight);
    [self fillRect:grayRegion withColor:[UIColor grayColor]];
    CGRect blueRegion = CGRectMake(segmentWidth * 2, cellHeight*5 + 30, segmentWidth, segmentHeight);
    [self fillRect:blueRegion withColor:[UIColor blueColor]];
    for (int row = 0; row < 5; row++) {
        for (int col = 0; col < 5; col++) {
            int i = row*5+col;
            CGRect rect = CGRectMake(cellWidth*col, cellHeight*row, cellWidth, cellHeight);
            if (_board->owners[i] == Mine) {
                [self fillRect:rect withColor:[UIColor redColor]];
            } else if (_board->owners[i] == Theirs) {
                [self fillRect:rect withColor:[UIColor blueColor]];
            } else {
                [self fillRect:rect withColor:[UIColor grayColor]];
            }
            [self strokeRect:rect withColor:[UIColor blackColor]];
            NSString *string = [NSString stringWithFormat:@"%c", _board->letters[i]];
            [string drawInRect:rect withFont:[UIFont fontWithName:@"Helvetica" size:(cellHeight / 2)]];
//            [string drawAtPoint:CGPointMake(0,0) withFont:[UIFont fontWithName:@"Helvetica" size:20]];
            [self drawChar:_board->letters[i] inRect:rect];
        }
    }
    [super drawRect:rect];
}


@end
