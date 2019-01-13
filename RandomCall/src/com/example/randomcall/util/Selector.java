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
	public List<Student> students = new LinkedList<Student>();		// 所有学生列表
	public List<Student> selected = new LinkedList<Student>();		// 选中学生列表
	
	/**
	 * 从流中读入学生名单
	 * @param fis	输入流
	 * @throws IOException
	 */
	public void loadStudents(InputStream fis) throws IOException {
		students.clear();
		InputStreamReader  inr = new InputStreamReader(fis, "UTF-8");	// 因为有中文，所以采用Reader类型的流来读，并需指定编码方式
		BufferedReader  in = new BufferedReader(inr);	// 套接一个BufferedReader流，可以一次读取一行
		String line = in.readLine();	// 读取标题行
		line = in.readLine();
		while(line != null) {
			line = line.trim();	//去除一行中前后空格
			if( line.length() > 0 )	{ //检查是否是空行
				Student	student = new Student();
				student.parseStudent(line);		// 解析一行，放入Student对象
				students.add(student);			// 再放入列表
			}
			line = in.readLine();
		}
		in.close();
		inr.close();
	}
	
	/** 打印选出的学生名单 */
	public void printSelected() {
		for(int i = 0; i < selected.size(); i++)	{
			System.out.println(selected.get(i));
		}
	}
	
	/**
	 * 随机选出一定个数的学生
	 * @param aCount	要选出的学生个数
	 */
	public void randomSelect(int aCount) {
		selected.clear();
		if(aCount > students.size()) aCount = students.size();
		if(aCount < 0) aCount = 0;
		Random rand = new Random();		// 使用了随机数发生器对象
		for(int i = 0; i < aCount; i++)	{
			int j = rand.nextInt(students.size());	// 随机产生一个[0, 学生总数) 之间的整数
			selected.add(students.remove(j));	// 把该下标对应的学生从students列表移入selected列表，防止同一学生重复选中
		}
		students.addAll(selected);	// 最后把选中的学生再重新放回students列表，保持students列表完整
	}
}
