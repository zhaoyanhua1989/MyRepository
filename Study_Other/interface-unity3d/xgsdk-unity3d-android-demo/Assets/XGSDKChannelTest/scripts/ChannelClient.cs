using System;
using System.IO;
using System.Net;
using System.Text;
using UnityEngine;

public class ChannelClient{

	public static string getResponse(string url, string datas){
		// If the url str is empty ,then return with empty str
		if (string.IsNullOrEmpty (url)) {
			return string.Empty;
		}
		Debug.Log ("#### Create WebRequest datas : " + datas);
		// Create a request using a URL that can receive a post. 
		WebRequest request = WebRequest.Create (url);
		Debug.Log ("#### Create WebRequest finished");
		// Set the Method property of the request to POST.
		request.Method = "POST";
		// Create POST data and convert it to a byte array.
		// string postData = "This is a test that posts this string to a Web server.";
		byte[] byteArray = Encoding.UTF8.GetBytes (datas);
		// Set the ContentType property of the WebRequest.
		request.ContentType = "application/json";
		// Set the ContentLength property of the WebRequest.
		request.ContentLength = byteArray.Length;
		// Get the request stream.
		Stream dataStream = request.GetRequestStream ();
		// Write the data to the request stream.
		dataStream.Write (byteArray, 0, byteArray.Length);
		// Close the Stream object.
		dataStream.Close ();
		// Get the response.
		Debug.Log ("#### send request");
		WebResponse response = request.GetResponse ();
		// Display the status.
		Console.WriteLine (((HttpWebResponse)response).StatusDescription);
		Debug.Log ("#### get response code : " + ((HttpWebResponse)response).StatusDescription);
		// Get the stream containing content returned by the server.
		dataStream = response.GetResponseStream ();
		// Open the stream using a StreamReader for easy access.
		StreamReader reader = new StreamReader (dataStream);
		// Read the content.
		string responseFromServer = reader.ReadToEnd ();
		// Display the content.
		Console.WriteLine (responseFromServer);
		// Clean up the streams.
		reader.Close ();
		dataStream.Close ();
		response.Close ();

		return responseFromServer;
	}
}
