# Lasca.py
# Reilly Watson, October-December 2007
# usage: python lasca.py

# Lasca is a checkers-like game.  The differences between Lasca and standard checkers are as follows:
# 1) The board is 7x7, rather than 8x8.
# 2) Captured pieces aren't removed from the board, but rather become prisoner to the capturing piece.
#    This results in stacks of pieces being built up.  On a jump, the topmost piece that has been jumped
#    is placed on the bottom of the stack of the jumping piece.
# 3) Jumps are forced, but there are no multi-jumps.
# 4) A stack is controlled by the player with the topmost piece on the stack.
# 5) The game ends when one player controls all the stacks, or when a player has no legal moves (stalemate)
# Note that kings remain kings even when they are captured, but the movement abilities of a stack
# are only determined by the topmost piece (ie if a king is the second-highest piece in a stack, that is useless)

# For more information on the rules, see http://research.interface.co.uk/lasca/about.htm

# To play against the computer, input your moves using the coordinate system (ie A5-B4).
# Coordinates are shown on the board.  To have the computer make a move for you, enter no move (just press return).
# Red and black pieces are represented by the letters r and b.  Kings are shown in caps.
# Due to the difficulty in displaying stacks of pieces on the grid, a list of the stacks
# for each row is shown to the right of the row.  These stacks are displayed from top to bottom.
# The previous board is also shown, to illustrate which move has just occurred.

# If you find you are receiving an illegal move message for a move that appears legal,
# check to make sure there isn't a jump you're overlooking.  Remember, jumps are forced!

# The current search depth is 12-ply.  If this proves too slow, it can be changed right here:
searchDepth = 12

# Sadly, this does not play a very good game of Lasca.  The problem here is the horizon effect:
# the computer thinks it's up a piece that it's about to lose.  The highly unstable nature of the
# game (there are captures on a high percentage of moves) means quiescence search would be extremely
# slow.

import string
import copy
import os
import time
import random

RedNonKing = "r"
RedKing = "R"
BlackNonKing = "b"
BlackKing = "B"

def IsPieceKing(piece):
	if piece == RedKing or piece == BlackKing:
		return True
	return False

def IsPieceRed(piece):
	if piece == RedNonKing or piece == RedKing:
		return True
	return False

def IsPieceBlack(piece):
	if piece == BlackNonKing or piece == BlackKing:
		return True
	return False

def IsSquareRed(square):
	if len(square) > 0:
		return IsPieceRed(square[0])
	return False

def IsSquareBlack(square):
	if len(square) > 0:
		return IsPieceBlack(square[0])
	return False

def IsSquareEmpty(square):
	if len(square) > 0:
		return False
	return True
	
def SquareMatchesTurn(square, turn):
	if (IsSquareBlack(square) and turn == "black") or (IsSquareRed(square) and turn == "red"):
		return True
	return False
	
def PieceMatchesTurn(piece, turn):
	if (IsPieceBlack(piece) and turn == "black") or (IsPieceRed(piece) and turn == "red"):
		return True
	return False
	
def OpposingSquares(square, othersquare):
	if (IsSquareBlack(square) and IsSquareRed(othersquare)) or (IsSquareRed(square) and IsSquareBlack(othersquare)):
		return True
	return False

def GetNextTurn(turn):
	if turn == "red":
		return "black"
	return "red"
	
def CoordsInBoard(row, col):
	if row < 0 or row > 6 or col < 0 or col > 3 or (col > 2 and row % 2 == 1):
		return False
	return True


# Swaps src and dest's contents
def SwapContents(src, dest):
	for x in range(len(src)):
		dest.insert(0, src.pop())
		
# Moves a piece, possibly promoting it to a king
def MovePiece(board, srcrow, srccol, destrow, destcol):
	SwapContents(board[srcrow][srccol], board[destrow][destcol])
	if destrow == 0 or destrow == 6:
		if IsSquareRed(board[destrow][destcol]):
			board[destrow][destcol][0] = RedKing
		if IsSquareBlack(board[destrow][destcol]):
			board[destrow][destcol][0] = BlackKing
		
		
# determines the final coordinates of a piece,
# given a start position and the position of the piece being jumped
def GetJumpDest(startrow, startcol, jumprow, jumpcol):
	xdir = 1
	ydir = 1
	if startrow > jumprow:
		ydir = -1
	if startcol > jumpcol or (startcol == jumpcol and startrow % 2 == 1):
		xdir = -1

	destrow = startrow + (2 * ydir)
	destcol = startcol + xdir
	
	return [destrow, destcol]

# determines the coordinates of the piece being jumped,
# given a start and finish position
def GetJumpedPieceCoords(startrow, startcol, destrow, destcol):
	jumprow = startrow
	jumpcol = startcol
	if startrow < destrow:
		jumprow = jumprow + 1
	else:
		jumprow = jumprow - 1
	if startcol < destcol and startrow % 2 == 1:
		jumpcol = jumpcol + 1
	if startcol > destcol and startrow % 2 == 0:
		jumpcol = jumpcol - 1
	
	return [jumprow, jumpcol]

def JumpPiece(board, startrow, startcol, jumprow, jumpcol):

	dest = GetJumpDest(startrow, startcol, jumprow, jumpcol)
	destrow = dest[0]
	destcol = dest[1]
	
	if not CoordsInBoard(destrow, destcol):
		return False
	
	if not OpposingSquares(board[startrow][startcol], board[jumprow][jumpcol]) or not IsSquareEmpty(board[destrow][destcol]):
		return False

	newboard = copy.deepcopy(board)
	capturedpiece = newboard[jumprow][jumpcol].pop(0)
	newboard[startrow][startcol].append(capturedpiece)
	MovePiece(newboard, startrow, startcol, destrow, destcol)
	return newboard

# returns a list of (row, col) pairs that are possible moves for this piece
def GetPossibleMoves(piece, row, col):
	moves = []
	if (IsPieceRed(piece) or IsPieceKing(piece)) and row > 0:
		moves.append([row - 1, col])
		if row % 2 == 0:
			if col > 0:
				moves.append([row - 1, col - 1])
		else:
			moves.append([row - 1, col + 1])
	if (IsPieceBlack(piece) or IsPieceKing(piece)) and row < 6:
		moves.append([row + 1, col])
		if row % 2 == 0:
			if col > 0:
				moves.append([row + 1, col - 1])
		else:
			moves.append([row + 1, col + 1])
	return moves

def AddJumpMoves(boards, board, row, col, jumprow, jumpcol):
	newboard = JumpPiece(board, row, col, jumprow, jumpcol)
	if newboard:
		boards.append(newboard)
	
def AddMove(boards, board, row, col, newrow, newcol):
	if newrow >= 0 and newrow < 7 and newcol >= 0:
		if newcol < len(board[newrow]):
			if IsSquareEmpty(board[newrow][newcol]):
				newboard = copy.deepcopy(board)
				MovePiece(newboard, row, col, newrow, newcol)
				boards.append(newboard)
			

# calls either AddMove or AddJumpMoves with the proper arguments
def CallMoveFn(fn, board, row, col):
	boards = []
	piece = board[row][col][0]
	moves = GetPossibleMoves(piece, row, col)
	for move in moves:
		fn(boards, board, row, col, move[0], move[1])
	return boards

# returns a list of boards that represent legal moves from this board
def GenerateMoves(board, turn):
	boards = []
	jump = False
	for row in range(len(board)):
		for col in range(len(board[row])):
			if not IsSquareEmpty(board[row][col]):
				if SquareMatchesTurn(board[row][col], turn):
					newjumpboards = CallMoveFn(AddJumpMoves, board, row, col)
					if len(newjumpboards) > 0:
						if not jump:
							jump = True
							# jumps are forced, so any nonjump moves
							# are now invalid
							boards = []
						boards.extend(newjumpboards)
					if not jump:
						newboards = CallMoveFn(AddMove, board, row, col)
						boards.extend(newboards)
	return boards

def MakeStartingBoard():
	board = MakeEmptyBoard()
	for i in range(3):
		AddPieces(board[i], BlackNonKing)
	for i in range(4, 7):
		AddPieces(board[i], RedNonKing)
	return board
		
def AddPieces(row, val):
	for x in row:
		x.append(val)
	
def MakeRow(num):
	a = []
	for i in range(num):
		a.append([])
	return a

def MakeEmptyBoard():
	return [MakeRow(4), MakeRow(3), MakeRow(4), MakeRow(3), MakeRow(4), MakeRow(3), MakeRow(4)]

def ClearScreen():
	if os.name == 'posix':
		os.system('clear')
	elif os.name == 'nt':
		os.system('cls')
	# else do nothing, because I don't know what system you're running
	# and so I can't clear the screen


def PrintBoard(board):
	print "  1 2 3 4 5 6 7       Stacks"
	x = "GFEDCBA"
	for i in range(len(board)):
		rowpieces = ""
		print x[i],
		if len(board[i]) == 3:
			print "-",
		for j in range(len(board[i])):
			if len(board[i][j]) > 0:
				print board[i][j][0],
				for stack in board[i][j]:
					rowpieces = rowpieces + stack
				rowpieces = rowpieces + " "
			else:
				print "+",
			if j < 3:
				print "-",
		print "     ", rowpieces
	print
	

# Pick a move at random.  Not currently used.
def MakeRandomMove(board, turn):
	boards = GenerateMoves(board, turn)
	if len(boards) == 0:
		return False
	return boards[random.randrange(0, len(boards))]

# Evaluate the board state
def EvaluateBoard(board, turn):
	# Strategy:
	# each piece you control is worth 1 point
	# kings are worth 1.3 times as much as regular pieces if on top, 1.1 times as much otherwise
	# a tower is worth 0.4 for every additional piece under your control
	# controlling an opponent's piece is worth 0.3 times the depth it is buried,
	# but only the first such piece is counted in each tower
	theirSquares = 0
	yourSquares = 0
	total = 0
	for row in range(len(board)):
		for col in range(len(board[row])):
			if len(board[row][col]) > 0:
				yourSquare = PieceMatchesTurn(board[row][col][0], turn)
				if yourSquare:
					yourSquares = yourSquares + 1
				else:
					theirSquares = theirSquares + 1
				for i in range(len(board[row][col])):
					piece = board[row][col][i]
					yourPiece = PieceMatchesTurn(piece, turn)
					multiplier = 1
					if not yourPiece:
						multiplier = -1
					if yourPiece != yourSquare:
						total = total + (0.3 * (-multiplier) * i)
						break
						
					if IsPieceKing(piece):
						if i == 0:
							total = total + (1.3 * multiplier)
						else:
							total = total + (1.1 * multiplier)
					else:
						total = total + multiplier
					if i > 0:
						total = total + (0.4 * multiplier)
	if theirSquares == 0:
		total = total + 1000 # victory condition
	if yourSquares == 0:
		total = total - 1000

	return total

# Select the best move.  Standard Minimax with Alpha-Beta pruning.
def Minimax(board, turn, depthlevel):
	value = MaxValue(board, turn, -10000, 10000, depthlevel)
	return value[1]
		
# Minimax's Max function.  Returns a [value, board] pair that is the
# best move for Max
def MaxValue(board, turn, alpha, beta, depthlevel):
	if depthlevel == 0:
		return [EvaluateBoard(board, turn), board]
	value = [-10000, board]
	successors = GenerateMoves(board, turn)
	if len(successors) == 1:
		return [EvaluateBoard(board, turn), successors[0]]
	for s in successors:
		minvalue = MinValue(s, GetNextTurn(turn), alpha, beta, depthlevel - 1)
		if minvalue[0] > value[0]:
			value = [minvalue[0], s]
		elif minvalue[0] == value[0]:
			select = random.random()
			if select >= 0.5:
				value = [minvalue[0], s]
		if value[0] >= beta:
			return value
		alpha = max(alpha, value[0])
	return value

# Minimax's Min function.  Returns a [value, board] pair that is the
# best move for Min
def MinValue(board, turn, alpha, beta, depthlevel):
	if depthlevel == 0:
		return [EvaluateBoard(board, turn), board]
	value = [10000, board]
	successors = GenerateMoves(board, turn)
	if len(successors) == 1:
		return [EvaluateBoard(board, turn), successors[0]]
	for s in successors:
		maxvalue = MaxValue(s, GetNextTurn(turn), alpha, beta, depthlevel - 1)
		if maxvalue[0] < value[0]:
			value = [maxvalue[0], s]
		elif maxvalue[0] == value[0]:
			select = random.random()
			if select >= 0.5:
				value = [maxvalue[0], s]
		if value[0] <= alpha:
			return value
		beta = min(beta, value[0])
	return value
	
# translate a row, col pair from E5-D4 format to array indices
def Trans(row, col):
	row = 71 - ord(row)
	col = col / 2
	if row % 2 == 1:
		col = col - 1
	return [row, col]
	
# Translates a move from user format to [srcrow, srccol, destrow, destcol] array indices
def TranslateCoordinates(coord):
	coord = coord.upper()
	rowcoord = coord[0]
	colcoord = string.atoi(coord[1])
	
	destrowcoord = coord[3]
	destcolcoord = string.atoi(coord[4])
	out = Trans(rowcoord, colcoord)
	out.extend(Trans(destrowcoord, destcolcoord))
	return out

def main():
	currentboard = MakeStartingBoard()
	humanplayer = "red"
	cpuplayer = "black"
	
	backupboard = currentboard
	
	turn = humanplayer
	nummoves = 0
	
	while True:
		ClearScreen()
		print "Last move's board:"
		PrintBoard(backupboard)
		print
		print "Current board:"
		PrintBoard(currentboard)
		nummoves+=1
		print "Move",nummoves
		print turn
		
		backupboard = currentboard
		
		if len(GenerateMoves(currentboard, turn)) == 0:
			print "Game over!"
			break
		
		if turn == cpuplayer:
			timestart = time.time()
			currentboard = Minimax(currentboard, turn, searchDepth)
			elapsed = time.time() - timestart
			if elapsed < 1:
				time.sleep(1 - elapsed)
			print "Move took", elapsed, "seconds"
		else:
			while True:
				humanmove = raw_input()
				
				if humanmove == "":
					currentboard = Minimax(currentboard, turn, searchDepth)
					break
				
				if humanmove == "quit":
					currentboard = 0
					break
				
				if humanmove == "eval":
					print "Board value for red:", EvaluateBoard(currentboard, "red")
					print "Board value for black:", EvaluateBoard(currentboard, "black")
					continue
				
				if len(humanmove) != 5:
					print "Moves must be in format RowCol-RowCol (ie D4-C3)"
					continue
				
				coords = TranslateCoordinates(humanmove)
				choices = GenerateMoves(currentboard, turn)
				srcrow = coords[0]
				srccol = coords[1]
				destrow = coords[2]
				destcol = coords[3]
				testboard = copy.deepcopy(currentboard)
				
				if CoordsInBoard(srcrow, srccol) and CoordsInBoard(destrow, destcol):
					if abs(srcrow - destrow) == 2:
						jumpedcoords = GetJumpedPieceCoords(srcrow, srccol, destrow, destcol)
						testboard = JumpPiece(testboard, srcrow, srccol, jumpedcoords[0], jumpedcoords[1])
					else:
						MovePiece(testboard, srcrow, srccol, destrow, destcol)
						
				if choices.count(testboard) == 0:
					print "Illegal move"
					print "Legal moves:", len(choices)
				else:
					currentboard = testboard
					break
			
		if not currentboard:
			break

		turn = GetNextTurn(turn)
		
	return backupboard
	

if __name__ == '__main__':
	main()
	
