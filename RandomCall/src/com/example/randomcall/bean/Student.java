package com.example.randomcall.bean;

import java.util.StringTokenizer;

public class Student{
	public String 	id;		// ѧ��
	public String 	name;	// ����
	public String	sex;	// �Ա�
	
	/**
	 * ��һ���ַ�����������ͬ�ֶΣ������Ӧ��Ա����
	 * @param line
	 */
	public void parseStudent(String line) {
		StringTokenizer st = new StringTokenizer(line);
		st.nextToken();		// �������
		st.nextToken();		// ����Ժϵ
		id = st.nextToken();	// ѧ��
		name = st.nextToken();	// ����
		sex = st.nextToken();	// �Ա�
	}
	
	public String toString(){
		return id + "\t" + name + "\t" + sex;
	}

}

