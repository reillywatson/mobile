//
//  BoardView.m
//  Letterpress Solver
//
//  Created by Reilly Watson on 2012-10-25.
//  Copyright (c) 2012 Reilly Watson. All rights reserved.
//

#import "BoardView.h"
#import "Evaluator.h"
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
    int cellWidth = self.frame.size.width / 5;
    int cellHeight = self.frame.size.height / 5;
    for (int row = 0; row < 5; row++) {
        for (int col = 0; col < 5; col++) {
            int i = row*5+col;
            CGRect rect = CGRectMake(cellWidth*col, cellHeight*row, cellWidth, cellHeight);
            UIColor *color;
            if (_board->owners[i] == Mine) {
                color = [UIColor blueColor];
                if ([Evaluator isProtected:i owners:_board->owners]) {
                    color = [UIColor blueColor];
                }
                else {
                    color = [UIColor colorWithRed:0.3 green:0.3 blue:1.0 alpha:1.0];
                }
            } else if (_board->owners[i] == Theirs) {
                if ([Evaluator isProtected:i owners:_board->owners]) {
                    color = [UIColor redColor];
                }
                else {
                    color = [UIColor colorWithRed:1.0 green:0.3 blue:0.3 alpha:1.0];
                }
            } else {
                color = [UIColor grayColor];
            }
            [self fillRect:rect withColor:color];
            [self strokeRect:rect withColor:[UIColor blackColor]];
            NSString *string = [NSString stringWithFormat:@"%c", _board->letters[i]];
            CGRect textRect = CGRectMake(rect.origin.x + rect.size.width/3, rect.origin.y+rect.size.height/3, rect.size.width, rect.size.height);
            [string drawInRect:textRect withFont:[UIFont fontWithName:@"Helvetica" size:(cellHeight / 2)]];
//            [string drawAtPoint:CGPointMake(0,0) withFont:[UIFont fontWithName:@"Helvetica" size:20]];
            [self drawChar:_board->letters[i] inRect:rect];
        }
    }
    [super drawRect:rect];
}


@end
