using System.IO;

public class PropertyFileOperator
{
	private StreamReader sr = null;
	/// <summary>
	/// 构造函数
	/// </summary>
	/// <param name="strFilePath">文件路径</param>
	public PropertyFileOperator(string strFilePath)
	{
		sr = new StreamReader(strFilePath);
	}
	
	/// <summary>
	/// 关闭文件流
	/// </summary>
	public void Close()
	{
		if (null != sr) {
			sr.Close();
			sr = null;
		}
	}
	
	/// <summary>
	/// 根据键获得值字符串
	/// </summary>
	/// <param name="strKey">键</param>
	/// <returns>值</returns>
	public string GetPropertiesText(string strKey)
	{
		string strResult = string.Empty;
		string str = string.Empty;
		sr.BaseStream.Seek(0, SeekOrigin.End);
		sr.BaseStream.Seek(0, SeekOrigin.Begin);
		while ((str = sr.ReadLine()) != null)
		{
			if (!string.Equals(str, string.Empty) && str.Substring(0,str.IndexOf('=')).Equals(strKey))
			{
				strResult = str.Substring(str.IndexOf('=')+1);
				break;
			}
		}
		return strResult;
	}
	
	/// <summary>
	/// 根据键获得值数组
	/// </summary>
	/// <param name="strKey">键</param>
	/// <returns>值数组</returns>
	public string[] GetPropertiesArray(string strKey)
	{
		string strResult = string.Empty;
		string str = string.Empty;
		sr.BaseStream.Seek(0, SeekOrigin.End);
		sr.BaseStream.Seek(0, SeekOrigin.Begin);
		while ((str = sr.ReadLine()) != null)
		{
			if (!string.Equals(str, string.Empty) && str.Substring(0, str.IndexOf('=')).Equals(strKey))
			{
				strResult = str.Substring(str.IndexOf('=')+1);
				break;
			}
		}
		return strResult.Split(',');
	}
}