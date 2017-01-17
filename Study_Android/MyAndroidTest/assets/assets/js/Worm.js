var UP = 0;
var DOWN = 1;
var LEFT = 2;
var RIGHT = 3;
var dir = 0;

function Worm() {

	this.nodes = [];
	this.nodes[this.nodes.length] = new Node(20, 10);
	this.nodes[this.nodes.length] = new Node(20, 11);
	this.nodes[this.nodes.length] = new Node(20, 12);
	this.nodes[this.nodes.length] = new Node(20, 13);
	this.nodes[this.nodes.length] = new Node(20, 14);
	this.nodes[this.nodes.length] = new Node(21, 14);
	this.nodes[this.nodes.length] = new Node(22, 14);
	this.nodes[this.nodes.length] = new Node(23, 14);
	this.nodes[this.nodes.length] = new Node(24, 14);
	this.nodes[this.nodes.length] = new Node(24, 15);
	this.nodes[this.nodes.length] = new Node(24, 16);
	this.nodes[this.nodes.length] = new Node(24, 17);
	this.nodes[this.nodes.length] = new Node(24, 18);
	this.nodes[this.nodes.length] = new Node(24, 19);

	// 判断传来的节点是否是蛇身子的一部分
	this.contains = function(i, j) {
		for (n = 0; n < this.nodes.length; n++) {
			var node = this.nodes[n];
			if (node.x == i && node.y == j) {
				return true;
			}
		}
		return false;
	};

	// 创建随机食物的方法
	this.randomFood = function() {
		var i;
		var j;
		do {
			i = Math.floor(Math.random() * ((screen.width - 20) / 10));
			j = Math.floor(Math.random() * 50);
		} while (this.contains(i, j));
		return new Node(i, j);
	};

	this.food = this.randomFood();

	// 向前走一步
	this.step = function() {
		var head = this.nodes[0]; // 老头节点
		var newHead;
		switch (dir) {
		case UP:
			newHead = new Node(head.x, head.y - 1);
			break;
		case DOWN:
			newHead = new Node(head.x, head.y + 1);
			break;
		case LEFT:
			newHead = new Node(head.x - 1, head.y);
			break;
		case RIGHT:
			newHead = new Node(head.x + 1, head.y);
			break;
		}
		// 判断新的头结点是否出界，这里判断的边界值和canvas的长宽相关，除以10是10为一格
		if (newHead.x < 0) {
			newHead.x = (screen.width - 20) / 10 - 1;
		} else if (newHead.x > (screen.width - 20) / 10 - 1) {
			newHead.x = 0;
		} else if (newHead.y < 0) {
			newHead.y = 49;
		} else if (newHead.y > 49) {
			newHead.y = 0;
		}

		// 在nodes数组的头部 追加一个node
		this.nodes.unshift(newHead);
		// 新节点是一个食物
		if (newHead.equals(this.food.x, this.food.y)) {
			// 重新随机一个food 计分
			this.food = this.randomFood();
		} else {
			// 尾部删除一个node
			this.nodes.pop();
		}
	}
}
