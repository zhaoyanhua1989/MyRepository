using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class ChannelParamCheckResultWindow : MonoBehaviour {

	public UIGrid grid;
	private GameObject item;
	public static string response;
	public static string interfaceName;
	public UILabel title;
	GameObject o ;
	void Start()
	{
		//获得UIgrid节点
		//grid = GameObject.Find("grid").GetComponent<UIGrid>();
		//example = GameObject.Find ("Item_uid");
		//panel = GameObject.Find("Panel-view").GetComponent<UIPanel>();
		//OnClick();
		//GameObject  Camera = GameObject.Find ("Camera");
		//o  = (GameObject) Instantiate(Resources.Load("paramsCheckWindow"));
		//o.transform.parent = Camera.transform;

		//if (!string.IsNullOrEmpty (response)) {
		//	this.displayResult(response, interfaceName);
		//}
	}

	//private void OnGUI()
	//{
		//顯示window，但是無法拖曳
		//GUI.Window(0, windowPosition, windowEvent, "不可拖曳");
		
		//顯示window，可以被拖曳
	//	GUI.Window(0,new Rect(200,129,100,100),funcwin,"window"); 
		
	//}

	//private void funcwin(int id)
	//{ 
	//	Texture tex  = (Texture) Instantiate(Resources.Load("Texture_window"));
	//	GUI.DrawTexture(new Rect(0, 0, 100, 100), tex);
	//}

	private void displayResult (string responseStr, string interfaceName)
	{
		Dictionary<string, object> responseObj = XGSDKMiniJSON.Json.Deserialize(responseStr) as Dictionary<string, object>;		

		title.text = interfaceName + " 参数校验结果";

		if(responseObj.ContainsKey("detail"))
		{
			List<System.Object> detailArr = responseObj["detail"] as List<System.Object>;
			// 由于动态生成表格还有问题，暂时写死30条记录
			if (null != detailArr && detailArr.Count > 0 && detailArr.Count < 30) {
				Dictionary<string, object> map;
				GameObject item;
				for(int i = 0; i < detailArr.Count; i++)
				{
					map = (Dictionary<string, object>)detailArr[i];
					//this.displayCheckResult(map);
					item = GameObject.Find ("item"+i);
					if (!item.activeSelf)
					{
						item.SetActive (true);
					}
					if(map.ContainsKey ("key")){
						item.GetComponentsInChildren<UILabel> () [0].text = map["key"] as string;
					}
					if(map.ContainsKey ("value")){
						item.GetComponentsInChildren<UILabel> () [1].text = map["value"] as string;
					}
					if(map.ContainsKey ("rule")){
						item.GetComponentsInChildren<UILabel> () [2].text = map["rule"] as string;
					}
					if(map.ContainsKey ("result")){
						if("1".Equals(map["result"].ToString())){
							item.GetComponentsInChildren<UISprite> () [0].spriteName = "yes";
						}else{
							item.GetComponentsInChildren<UISprite> () [0].spriteName = "no";
						}
					}
				}
				//隐藏暂时不用的items
				for(int j = detailArr.Count ; j <= 30; j++)
				{
					item = GameObject.Find ("item"+j);

					if (item.activeSelf)
					{
						item.SetActive (false);
					}
				}

			} else {
				Debug.LogError ("can not parse response string ");
			}
		}
		//for (int i = 0; i<200; i++) {
			//克隆预设
		//	GameObject o  =(GameObject) Instantiate(example);
			//为每个预设设置一个独一无二的名称
		//	o.name = "Item_" + i;
			//将新预设放在Panel对象下面
		//	o.transform.parent = grid.transform;
		//}		
		//列表添加后用于刷新listView
		//grid.repositionNow = true;
	}

	public void goBack(){
		ChannelUtils.DialogHelper.getInstance ().showCallbackTip ("Exit channel params check page");
	}
	
}
