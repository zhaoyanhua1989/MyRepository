using System;
using System.Collections;
using System.Collections.Generic;

public class ConfigsUtils
{
	private static Dictionary<string, string> configs = new Dictionary<string, string>();

	private static string readPropertiesText(string key){
		PropertyFileOperator properties = new PropertyFileOperator ("Assets/XGSDKChannelTest/resources/configs/sdk_config.properties");
		string value = properties.GetPropertiesText (key);
		properties.Close ();
		return value;
	}

	public static string getTextByKey(string key){
		string result = string.Empty;
		if (configs.ContainsKey (key)) {
			result = configs[key];
		}

		if (string.IsNullOrEmpty (result)) {
			result = readPropertiesText (key);
			if(configs.ContainsKey(key)){
				configs.Remove(key);
			}
			configs.Add(key, result);
		}
		return result;
	}

	public static void put(string key, string value){
		if (!string.IsNullOrEmpty (key)) {
			if(configs.ContainsKey(key)){
				configs.Remove(key);
			}
			configs.Add (key, value);
		}
	}

}