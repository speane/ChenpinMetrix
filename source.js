// Copyright (c) 1996-1997 Tomer Shiran. All rights reserved.
// Permission given to use the script provided that this notice remains as is.
// Additional scripts can be found at http://www.geocities.com/~yehuda/

// array to hold number of shapes used from each type


var statistics = new Array(7)
for (var shapeNum = 0; shapeNum < 7; ++shapeNum) {
	statistics = 0
}

// set pause to false
var paused = false

// game is currently running
var timerRunning = false

// no shape currently falling
var shape = -1

// timer is not running
var timerID = null

statistics = 10

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

// set image URLs
/*on[0].src = "10.gif"
on[1].src = "11.gif"
on[2].src = "12.gif"
on[3].src = "13.gif"
on[4].src = "14.gif"
on[5].src = "15.gif"
on[6].src = "16.gif"
off.src = "0.gif"*/

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

function state(x, y) {
	// assign URL of image at given coordinates to local variable
	var source = document.images[computeIndex(x, y)].src

	// sexpression evaluates to 0 or 1
	//return (source.charAt(source.lastIndexOf('/') + 1) == '0') ? false : true
}

// set square to 1 / 0
function setSquare(x, y, state) {
	if (state == 0)
		document.images[computeIndex(x, y)].src = off.src
	else
		document.images[computeIndex(x, y)].src = on[shape].src

	// if state is 1 square is active, so 1 is assigned to ar[x][y]
 	// otherwise square is not active so 0 is assigned to ar[x][y]
	ar[x][y] = state
}