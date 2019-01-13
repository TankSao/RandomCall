package com.example.randomcall.bean;

import java.io.Serializable;

public class CallDetail implements Serializable{
	private static final long serialVersionUID = -4923046188593506660L;
	public static String[] STATE = {"正常", "旷课", "请假", "迟到"}; 
	
	public String 	studentId;		// 学号
	public String 	name;			// 姓名
	public String	sex;			// 性别
	public int _id;					// 数据库记录编号，修改记录时用
	public String state = STATE[0];	// 点名状态：正常、旷课、请假、迟到
	public int callId = 0;				// 点名id，对应Call的id，同一次点名中的callId相同 
	public boolean modified = false;	// 是否编辑过
	public String zp;                   //学生照片，用于验证答道的是否为学生本人
	public String toString(){
		return name + "," + studentId + ", " + sex + ", " + state + ", " + callId + ", " + modified;
	}
}
