package com.example.randomcall.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.example.randomcall.bean.Student;

public class Selector {
	public List<Student> students = new LinkedList<Student>();		// ����ѧ���б�
	public List<Student> selected = new LinkedList<Student>();		// ѡ��ѧ���б�
	
	/**
	 * �����ж���ѧ������
	 * @param fis	������
	 * @throws IOException
	 */
	public void loadStudents(InputStream fis) throws IOException {
		students.clear();
		InputStreamReader  inr = new InputStreamReader(fis, "UTF-8");	// ��Ϊ�����ģ����Բ���Reader���͵�������������ָ�����뷽ʽ
		BufferedReader  in = new BufferedReader(inr);	// �׽�һ��BufferedReader��������һ�ζ�ȡһ��
		String line = in.readLine();	// ��ȡ������
		line = in.readLine();
		while(line != null) {
			line = line.trim();	//ȥ��һ����ǰ��ո�
			if( line.length() > 0 )	{ //����Ƿ��ǿ���
				Student	student = new Student();
				student.parseStudent(line);		// ����һ�У�����Student����
				students.add(student);			// �ٷ����б�
			}
			line = in.readLine();
		}
		in.close();
		inr.close();
	}
	
	/** ��ӡѡ����ѧ������ */
	public void printSelected() {
		for(int i = 0; i < selected.size(); i++)	{
			System.out.println(selected.get(i));
		}
	}
	
	/**
	 * ���ѡ��һ��������ѧ��
	 * @param aCount	Ҫѡ����ѧ������
	 */
	public void randomSelect(int aCount) {
		selected.clear();
		if(aCount > students.size()) aCount = students.size();
		if(aCount < 0) aCount = 0;
		Random rand = new Random();		// ʹ�������������������
		for(int i = 0; i < aCount; i++)	{
			int j = rand.nextInt(students.size());	// �������һ��[0, ѧ������) ֮�������
			selected.add(students.remove(j));	// �Ѹ��±��Ӧ��ѧ����students�б�����selected�б���ֹͬһѧ���ظ�ѡ��
		}
		students.addAll(selected);	// ����ѡ�е�ѧ�������·Ż�students�б�����students�б�����
	}
}
