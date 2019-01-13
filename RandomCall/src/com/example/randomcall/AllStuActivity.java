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
	// TODO �Զ����ɵķ������
	super.onCreate(savedInstanceState);
	setContentView(R.layout.all_student);
	lv_all=(ListView) findViewById(R.id.lv_all_student);
	bj_title=(TextView) findViewById(R.id.bj_title);
	selector = new Selector();
	// ���ı��ļ��ж���ѧ�����������ö�ȡ�ļ��ı�׼����try..catch..finally
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
		Toast.makeText(AllStuActivity.this, "��ѡ��༶�����쳣��������ѡ�񣡣���", Toast.LENGTH_SHORT).show();
	}else{
		if(bj==1){
			adapter = new MyAdapter(this, ds1);
			bj_title.setText("����һ������");
		}
		if(bj==2){
			adapter = new MyAdapter(this, ds2);
			bj_title.setText("���̶�������");
		}
		lv_all.setAdapter(adapter);
	}
}
}
