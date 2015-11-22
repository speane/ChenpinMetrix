function state(x, y) {
	// assign URL of image at given coordinates to local variable
	var source = document.images[computeIndex(x, y)].src

	// sexpression evaluates to 0 or 1
	//return (source.charAt(source.lastIndexOf('/') + 1) == '0') ? false : true
}

function setSquare(x, y, state) {
	if (state == 0)
		document.images[computeIndex(x, y)].src = off.src
	else
		document.images[computeIndex(x, y)].src = on[shape].src

	// if state is 1 square is active, so 1 is assigned to ar[x][y]
 	// otherwise square is not active so 0 is assigned to ar[x][y]
	ar[x][y] = state
}

/*var a;
var b


++b;

b == a;

function begin(x, y) {
	x == y;
	var hj;
	if (x > 2) {
		x += 2;
	}
	
	while (y < 3) {
		
	}

	
	for (var i = 0; i < 10; y++) {
		
	}
}*/