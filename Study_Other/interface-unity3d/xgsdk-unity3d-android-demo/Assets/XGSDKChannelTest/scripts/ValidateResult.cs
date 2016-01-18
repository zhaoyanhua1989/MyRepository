using UnityEngine;
using System.Collections;
// validation result class
public class ValidateResult{

	private bool isValid;
	private string validationMsg;

	public ValidateResult(bool flag, string msg){
		this.setIsValid(flag);
		this.setValidationMsg(msg);
	}

	public void setIsValid(bool flag){
		isValid = flag;
	}

	public void setValidationMsg(string msg){
		validationMsg = msg;
	}

	public bool getIsValid(){
		return this.isValid;
	}

	public string getValidationMsg(){
		return this.validationMsg;
	}
}
