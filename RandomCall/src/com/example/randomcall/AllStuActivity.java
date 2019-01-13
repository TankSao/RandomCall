package com.example.randomcall;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.randomcall.adapter.MyAdapter;
import com.example.randomcall.util.Selector;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AllStuActivity extends Activity{
	private List all=new ArrayList();
	private List ds1=new ArrayList();
	private List ds2=new ArrayList();
	private Selector selector;
	private int bj;
	private ListView lv_all;
	private MyAdapter adapter;
	private TextView bj_title;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO 自动生成的方法存根
	super.onCreate(savedInstanceState);
	setContentView(R.layout.all_student);
	lv_all=(ListView) findViewById(R.id.lv_all_student);
	bj_title=(TextView) findViewById(R.id.bj_title);
	selector = new Selector();
	// 从文本文件中读入学生名单，采用读取文件的标准流程try..catch..finally
	InputStream fis = null;
	try{
		//fis = openFileInput("students.txt");
		fis = getAssets().open("students_utf8.txt");
		selector.loadStudents(fis);
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if(fis!=null) {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	for(int i=0;i<selector.students.size();i++){
		String xsid=selector.students.get(i).id;
		if(xsid.startsWith("201318141")){
			ds1.add(selector.students.get(i));
		}else{
			ds2.add(selector.students.get(i));
		}
	}
	bj=getIntent().getIntExtra("bj", -1);
	if(bj<0){
		Toast.makeText(AllStuActivity.this, "所选择班级存在异常，请重新选择！！！", Toast.LENGTH_SHORT).show();
	}else{
		if(bj==1){
			adapter = new MyAdapter(this, ds1);
			bj_title.setText("电商一班名单");
		}
		if(bj==2){
			adapter = new MyAdapter(this, ds2);
			bj_title.setText("电商二班名单");
		}
		lv_all.setAdapter(adapter);
	}
}
}
