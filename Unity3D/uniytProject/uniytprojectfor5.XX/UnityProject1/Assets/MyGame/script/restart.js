#pragma strict

function Start () {

}

function Update () {

}

function OnGUI() {
	// 创建按钮，Rect矩形按钮(距离屏幕左边距离，距离屏幕上边距离，长，宽)
	if(GUI.Button(Rect(180,100,60,30), "退出")){
		Application.Quit();
	}
	if(GUI.Button(Rect(280,100,60,30), "重新开始")){
		// Application.LoadLevel("sheji"); // 旧版本加载新页面
		SceneManagement.SceneManager.LoadScene("sheji"); // 新版本加载新页面
	}
	if(GUI.Button(Rect(10,160,100,50), "Play")){
		// 开始播放音乐，从头开始
		GetComponent.<AudioSource>().Play();
	}
	if(GUI.Button(Rect(10,220,100,50), "Pause")){
		GetComponent.<AudioSource>().Pause();
	}
	if(GUI.Button(Rect(10,280,100,50), "Stop")){
		GetComponent.<AudioSource>().Stop();
	}
}