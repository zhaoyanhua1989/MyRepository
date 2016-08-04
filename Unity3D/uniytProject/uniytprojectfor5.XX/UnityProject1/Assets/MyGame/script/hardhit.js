#pragma strict

function Start () {
	
}

// 次物体被击中次数
var mhitn : int;

function Update () {
	// 如果物体y轴小于1(说明被打倒地面以下了)，物体消失
	if(gameObject.transform.position.y < 0){
		var hitn : int = ++gameObject.Find("Main Camera").GetComponent(sheji).hitn;
		// 0.5秒后销毁此物体
		Destroy(gameObject, 0.5);
	}
}

// 物体的撞击事件，初始时会产生和地面的撞击事件
function OnCollisionEnter(theobject:Collision){
	// 和子弹的撞击事件
	if(theobject.gameObject.name=="zidan(Clone)"){
		mhitn++;
		print("mhitn="+mhitn);
		// 如果物体被击中4次，则销毁
		if(mhitn==4){
			gameObject.Find("Main Camera").GetComponent(sheji).hitn++;
			// 0.5秒后销毁此物体
			Destroy(gameObject, 0.5);
		}
	}
}