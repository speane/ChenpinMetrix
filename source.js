// Copyright (c) 1996-1997 Tomer Shiran. All rights reserved.
// Permission given to use the script provided that this notice remains as is.
// Additional scripts can be found at http://www.geocities.com/~yehuda/

// array to hold number of shapes used from each type
var statistics = new Array(7)
for (var shapeNum = 0; shapeNum < 7; ++shapeNum) {
	statistics[shapeNum] = 0
}

// set pause to false
var paused = false

// game is currently running
var timerRunning = false

// no shape currently falling
var shape = -1

// timer is not running
var timerID = null

// initialize image variables for seven shapes
var on = new Array()
on[0] = new Image(12, 12)
on[1] = new Image(12, 12)
on[2] = new Image(12, 12)
on[3] = new Image(12, 12)
on[4] = new Image(12, 12)
on[5] = new Image(12, 12)
on[6] = new Image(12, 12)

// create a transparent block
var off = new Image(12, 12)

// get number of images already laid out in the page
var firstImage = document.images.length

// create initial screen
drawScreen()

// array of screen (10 x 19)
var ar = new Array()
for (var i = 0; i < 10; ++i) {
	ar[i] = new Array(19)
	for (var j = 0; j < 19; ++j) {
		ar[i][j] = 0
	}
}

function computeIndex(x, y) {
        return (y * 10 + x) + firstImage
}

function state(x, y) {
	// assign URL of image at given coordinates to local variable
	var source = document.images[computeIndex(x, y)].src

	// expression evaluates to 0 or 1
	return (source.charAt(source.lastIndexOf('/') + 1) == '0') ? false : true
}

function clearActive() {
	// scan entire array and assign 0 to all elements (no active squares)
	for (var i = 0; i < 10; ++i) {
		for (var j = 0; j < 19; ++j) {
			ar[i][j] = 0
		}	
	}

	// no shape is currently in screen
	shape = -1
}

// check if specified move (left or right) is valid

function checkMoveX(step) {
	// scan screen (direction does not matter)
	for (var x = 0; x < 10; ++x) {
		for (var y = 0; y < 19; ++y) {
			// if current square is active
			if (ar[x][y] == 1) {
				// check all conditions:
				// not out of range and not coliding with existing not active block
				if (x + step < 0 || x + step > 9 || (state(x + step, y) && ar[x + step][y] == 0))
					// return false if move (new situation) is not legal
					return false
			}
		}
	}
	
	// return true if no invalid state has been encountered
	return true
}

// check if specified move (down) is valid
function checkMoveY() {
	// only possible step is one to the bottom
	var step = 1

	// scan screen (direction does not matter)
	for (var x = 0; x < 10; ++x) {
		for (var y = 0; y < 19; ++y) {
			// if current square is active
			if (ar[x][y] == 1) {
				// check all conditions:
				// not out of range and not coliding with existing not active block
				if (y + step > 18 || (state(x, y + step) && ar[x][y + step] == 0))
					// return false if move (new situation) is not legal
					return false
			}
		}
	}
	
	// return true if no invalid state has been encountered
	return true
}


// move all active squares step squares on the x axis
function moveX(step) {
	// if specified move is not legal
	if (!checkMoveX(step))
		// terminate function (active blocks are not moved)
		return

	// if left movement then scan screen from left to right
	if (step < 0) {
		for (var x = 0; x < 10; ++x) {
			for (var y = 0; y < 19; ++y) {
				// if current square is active
				if (ar[x][y] == 1)
					// call function to handle movement
					smartX(x, y, step)
			}
		}
	} else

	// if right movement then scan screen from right to left
	if (step > 0) {
		for (var x = 9; x >= 0; --x) {
			for (var y = 0; y < 19; ++y) {
				// if current square is active
				if (ar[x][y] == 1)
					// call function to handle movement
					smartX(x, y, step)
			}
		}
	}
}
// responsible for the blocks' horizontal movement
function smartX(x, y, step) {
	// if moving one step to the left
	if (step < 0)
		// if the destination square needs to be turned on explicitly
		if (ar[x + step][y] == 0)
			// if there is a block to the right of the current block
			if (x != 9 && ar[x - step][y] == 1)
				// set square to the left on without clearing current block
				setSquare(x + step, y, 1)
			else
				// clear current block and turn square to the left on
				warp(x, y, x + step, y)
		else
			// if there is no block to the right of the current block
			if (x == 9 || ar[x - step][y] == 0)
				// clear current block
				setSquare(x, y, 0)

	// if moving one step to the right
	if (step > 0)
		// if the destination square needs to be turned on explicitly
		if (ar[x + step][y] == 0)
			// if there is a block to the left of the current block
			if (x != 0 && ar[x - step][y] == 1)
				// set square to the right on without clearing current block
				setSquare(x + step, y, 1)
			else
				// clear current block and turn square to the right on
				warp(x, y, x + step, y)
		else
			// if there is no block to the left of the current block
			if (x == 0 || ar[x - step][y] == 0)
				// clear current block
				setSquare(x, y, 0)
}

// move all active squares step squares on the x axis
function moveY() {
	// if specified move is not legal (shape is laid down on block or bottom panel)
	if (!checkMoveY()) {
		// active squares are not active anymore (should not be moved later)
		clearActive()

		// terminate function (active blocks are not moved)
		return
	}

	// scan screen from bottom to top
	for (var y = 18; y >= 0; --y) {
		for (var x = 0; x < 10; ++x) {
			// if current square is active
			if (ar[x][y] == 1)
				// call function to handle movement
				smartY(x, y)
		}
	}
}

// responsible for the blocks' vertical (downwards) movement
function smartY(x, y) {
	// if the destination square needs to be turned on explicitly
	if (ar[x][y + 1] == 0)
		// if there is a block above current block
		if (y != 0 && ar[x][y - 1] == 1)
			// set square below on without clearing current block
			setSquare(x, y + 1, 1)
		else
			// clear current block and turn square below on
			warp(x, y, x, y + 1)
	else
		// if there is no block above the current block
		if (y == 0 || ar[x][y - 1] == 0)
			// clear current block
			setSquare(x, y, 0)
}

function checkWarp(startX, startY, endX, endY) {
	// if a destination coordinate is invalid or destination square is off
	// state(endX, endY) must be last due to short-circuit evaluation
	if (endX < 0 || endX > 9 || endY < 0 || endY > 18 || state(endX, endY))
		// return false because warp is invalid
		return false

	// return true because warp has not been proved to be invalid (it is valid)
	return true
}

// return true if no active squares are found and false otherwise
function noActive() {
	// scan board from top to bottom
	for (var y = 0; y < 19; ++y) {
		for (var x = 0; x < 10; ++ x) {
			if (ar[x][y] == 1)
				return false
		}
	}

	// no active square found on the board
	return true
}

// return true if the line with the given coordinate is completed
function isLine(y) {
	// horizontal scan of current line
	for (var x = 0; x < 10; ++x) {
		// if a square is off the line is not completed
		if (!state(x, y))
			return false
	}
	
	// no square was found off
	return true
}