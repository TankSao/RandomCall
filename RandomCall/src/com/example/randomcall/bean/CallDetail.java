package com.example.randomcall.bean;

import java.io.Serializable;

public class CallDetail implements Serializable{
	private static final long serialVersionUID = -4923046188593506660L;
	public static String[] STATE = {"����", "����", "���", "�ٵ�"}; 
	
	public String 	studentId;		// ѧ��
	public String 	name;			// ����
	public String	sex;			// �Ա�
	public int _id;					// ���ݿ��¼��ţ��޸ļ�¼ʱ��
	public String state = STATE[0];	// ����״̬�����������Ρ���١��ٵ�
	public int callId = 0;				// ����id����ӦCall��id��ͬһ�ε����е�callId��ͬ 
	public boolean modified = false;	// �Ƿ�༭��
	public String zp;                   //ѧ����Ƭ��������֤������Ƿ�Ϊѧ������
	public String toString(){
		return name + "," + studentId + ", " + sex + ", " + state + ", " + callId + ", " + modified;
	}
}
