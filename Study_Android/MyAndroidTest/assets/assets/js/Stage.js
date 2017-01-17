function Stage () {
	this.width=50;
	this.height=50;
	this.worm=new Worm();	
	
	//用于绘制舞台的方法
	this.print=function (ctx) {
		for(i=0; i<this.width; i++){
			for(j=0; j<this.height; j++){
				if(this.worm.contains(i, j)){
					//蛇身子的一部分
					ctx.fillStyle="#1111ef";
					ctx.fillRect(i*10, j*10, 10, 10);
				}else if(this.worm.food.equals(i, j)){
					ctx.fillStyle="#000000";
					ctx.fillRect(i*10, j*10, 10, 10);
				}else{
					ctx.fillStyle="#abcdef";
					ctx.fillRect(i*10, j*10, 10, 10);
				}
			} 
		}
	};
}