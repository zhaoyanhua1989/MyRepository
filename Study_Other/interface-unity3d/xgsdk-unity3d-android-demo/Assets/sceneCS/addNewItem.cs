using UnityEngine;
using System.Collections;

public class addNewItem : MonoBehaviour {

	public UIGrid orderListGrid;

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}


	public void addItem()
	{
		GameObject orderGameObject = GameObject.Find ("orderList_Grid");
		int size = orderListGrid.GetChildList ().Count;
		Debug.Log ("size is " + size);
		foreach(Transform child in orderListGrid.GetChildList())
		{
			Debug.Log (child.name);
		}

		GameObject gridItem = NGUITools.AddChild (orderGameObject, (GameObject)(Resources.Load ("Item-demo")));
		gridItem.name = "new item " + (size+1);

		Transform[] itemChilds = gridItem.GetComponentsInChildren<Transform> ();
		foreach(Transform child in itemChilds)
		{
			Debug.Log ("dddd" + child.gameObject.tag);

			if(child.name == "orderNo")
			{

				UILabel orderNo = child.gameObject.GetComponent<UILabel>();
				orderNo.text = "123456";
			}

		}



		orderListGrid.repositionNow = true;
		orderListGrid.Reposition ();                                                          


	}
}
