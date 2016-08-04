#pragma strict

function Start () {
	// 3秒后销毁，把该脚本添加在scene中的物体对象上
	// Destroy(gameObject, 0.5);
}

function Update () {

}

// 子弹的撞击事件
function OnCollisionEnter(theobject:Collision){
	// 如果子弹撞击到的物体不是地面，则被摧毁
	if(theobject.gameObject.name!="dimian"){
		print("撞击到："+theobject.gameObject.name);
		Destroy(gameObject);
	}
}