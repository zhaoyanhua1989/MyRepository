#pragma strict

// 场景加载时执行，只一次
function Start () {

}

// 视角运动每秒多少米
var speed : int = 5;
// 点击鼠标时创建的物体
var newobject : Transform;
// 记录发射子弹的数量
var firen : int;
// 摧毁物体的个数
var hitn : int;

// 获取视角的x坐标
var cx: float = 0.0f;
// 获取视角的y坐标
var cy: float = 0.0f;
// 获取视角的z坐标
var cz: float = 0.0f;

// 游戏每一帧就要被调用
function Update () {
	// 获取当前的坐标位置
	getPosition();

	// 实现主摄像机移动
	doMove();

	// 发射子弹
	doFire();

	//　实现主摄像机的旋转
	doRotate();

	// 检查有没有通关
	checkSuccess();
}

function doMove(){
	// 拿到x轴移动数据
    // Time.deltaTime: 完成最后一帧的时间，也可以解释为两帧之间的时间。如果你加或减一个每帧改变的值，你应该与Time.deltaTime相乘，当你乘以Time.deltaTime实际表示：每秒移动物体10米，而不是每帧10米。
    // 拿到x轴移动数据，左右移动
	var x : float = Input.GetAxis("Horizontal")* Time.deltaTime *speed;
	// 拿到y轴移动数据，前后移动
	var z : float = Input.GetAxis("Vertical")* Time.deltaTime *speed;
	// 设置跳跃移动数据，上下移动
	var y : float = Input.GetAxis("Jump")* Time.deltaTime *1;

	// 使物体移动
	transform.Translate(x, y, z);
	// 输出打印x变化的数据，在输出台上看
	// print("jump:"+y);
}

function doFire(){
	// 检测有没有按鼠标左键
	if(Input.GetButtonDown("Fire1")){
		// 在主摄像机位置生成一个物体
		var n: Transform = Instantiate(newobject, transform.position, transform.rotation);
		// 发射子弹的位置转换(有一下两种方式获取)
		// var fwd: Vector3 = transform.TransformDirection(Vector3.forward); // 返回以物体自身为坐标轴的向量direction在世界坐标中的朝向向量
		var fwd: Vector3 = transform.forward; // 物体自身的蓝色轴向（Z轴）在世界坐标中所指向的位置(一般是指向前方)，一般角色扮演常用
		// 子弹添加力
		n.GetComponent.<Rigidbody>().AddForce(fwd*2800);

		firen++;

	}
}

function doRotate(){
	// 键盘监听来方式旋转
	if(Input.GetKey(KeyCode.Q)) {
		transform.Rotate(0, -200*Time.deltaTime, 0, Space.Self); // 旋转方向：x轴，y轴，z轴
	}
	if(Input.GetKey(KeyCode.E)) {
		transform.Rotate(0, 200*Time.deltaTime, 0, Space.Self);
	}
	// 通过鼠标来转动视角
	var RoatedSpeed: float = 200.0f;
	//if(Input.GetMouseButton(1)){//控制左右旋转
	//	transform.Rotate(0, Input.GetAxis("Mouse X")*RoatedSpeed*Time.deltaTime, 0);
	//	transform.Rotate(0, Input.GetAxis("Mouse X")*RoatedSpeed*Time.deltaTime, 0);
	//}
	//控制上下左右旋转
	if(Input.GetMouseButton(1)){
		transform.Rotate(Vector3.up, -Time.deltaTime * 200 * Input.GetAxis("Mouse X"));
		transform.Rotate(Vector3.right, Time.deltaTime * 200 * Input.GetAxis("Mouse Y"));
	}
}

function getPosition(){
	// 获取Player变量指定的对象的三围坐标
	var player_postion: Vector3 = transform.position;
	// 获取X,Y,Z值
	cx = player_postion.x;
	cy = player_postion.y;
	cz = player_postion.z;
	// print("当前位置: ("+ cx + "," + cy + "," + cz + ")");

	if(cy <= 1){
		// 当主摄像机的高度是1时，修正Z轴的倾斜，使y轴方向总是和x轴垂直
 		var eulerAngle :Vector3 = transform.rotation.eulerAngles ; 
 		eulerAngle.z = 0 ; 
 		transform.rotation = Quaternion.Euler( eulerAngle ) ;
	}

	if(cy <= 1){
		// 当主摄像机的高度是1时，修正x轴的倾斜，使y轴方向总是和x轴垂直
 		var xAngle :Vector3 = transform.rotation.eulerAngles ; 
 		xAngle.x = 0 ; 
 		transform.rotation = Quaternion.Euler( xAngle ) ;
	}
}

function checkSuccess() {
	// 先找到组件名，然后找到组件类型，再设置值(GameObject是该脚本绑定的对象)
	GameObject.Find("text").GetComponent(UI.Text).text="发射子弹数：" + firen + " ,销毁物体个数：" + hitn;
	// 当摧毁的物体达到10个时，过关
	if(hitn>=10){
		gameObject.Find("text").GetComponent(UI.Text).text="恭喜过关";
		gameObject.Find("Main Camera").GetComponent(sheji).enabled=false;
		// restart的脚本一开始并没有启用，没有点勾
		gameObject.Find("dimian").GetComponent(restart).enabled=true;
	}
}

function unknown(){
	// 产生一条射线(从摄像机开始，沿着我们点击的点发射出去)，然后Raycast 会检测这个射线是否碰撞到物体，如果碰撞返回true
	if(Input.GetMouseButtonDown(0)){
		var ray : Ray = Camera.main.ScreenPointToRay(Input.mousePosition);
		if (Physics.Raycast(ray)){
			Debug.Log("okok");
		}
    }
}