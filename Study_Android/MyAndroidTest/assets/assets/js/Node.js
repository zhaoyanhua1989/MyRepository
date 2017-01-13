function Node (x, y) {
	this.x=x;
	this.y=y;
	
	this.equals=function (i, j) {
		return this.x==i && this.y==j;
	};
}