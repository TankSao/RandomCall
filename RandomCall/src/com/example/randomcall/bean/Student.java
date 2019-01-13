package com.example.randomcall.bean;

import java.util.StringTokenizer;

public class Student{
	public String 	id;		// 学号
	public String 	name;	// 姓名
	public String	sex;	// 性别
	
	/**
	 * 将一行字符串解析出不同字段，存入对应成员变量
	 * @param line
	 */
	public void parseStudent(String line) {
		StringTokenizer st = new StringTokenizer(line);
		st.nextToken();		// 跳过序号
		st.nextToken();		// 跳过院系
		id = st.nextToken();	// 学号
		name = st.nextToken();	// 姓名
		sex = st.nextToken();	// 性别
	}
	
	public String toString(){
		return id + "\t" + name + "\t" + sex;
	}

}

