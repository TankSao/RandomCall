package com.example.randomcall;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.randomcall.bean.CallDetail;
import com.example.randomcall.bean.Student;
import com.example.randomcall.util.Selector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	Selector selector;
	private Button sysm;
	private Button btn_ds1,btn_ds2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initComponent();
		initSelector();
		initListener();
	}
	private void initListener() {
		// TODO �Զ����ɵķ������
		btn_ds1.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Intent intent = new Intent(MainActivity.this, AllStuActivity.class);
				intent.putExtra("bj", 1);
				startActivity(intent);
			}
		});
       btn_ds2.setOnClickListener(new OnClickListener() {
	
	        public void onClick(View v) {
		       // TODO �Զ����ɵķ������
	        	Intent intent = new Intent(MainActivity.this, AllStuActivity.class);
				intent.putExtra("bj", 2);
				startActivity(intent);
	        }
        });
       sysm.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				String sm="ע�⣺�����б������Ը��ĵ���״̬������Ϊ�˷�ֹ��Ρ�������������ĳ��֣������б�ĳһ��ѧ������Ϣ�����Ե�����ѧ������Ƭ������";
				Toast.makeText(MainActivity.this, sm, Toast.LENGTH_SHORT).show();
			}
		});

       Button btnSelect = (Button) findViewById(R.id.buttonCall);
		btnSelect.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				if(selector.students.size()==0) {
					Toast.makeText(MainActivity.this, "ѧ������Ϊ��", Toast.LENGTH_LONG).show();
					return;
				}
				
				// ����һ��AlertDialog�������������
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("�������������");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				final EditText numberET = new EditText(MainActivity.this);
				numberET.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
				numberET.setText("10");
				builder.setView(numberET);
				builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface arg0, int arg1) {
						int count = Integer.parseInt(numberET.getText().toString());
						if(count<1) {
							Toast.makeText(MainActivity.this, "���ٵ�1��", Toast.LENGTH_SHORT).show();
							return;
						}
						if(count>selector.students.size()) {
							Toast.makeText(MainActivity.this, "���ܳ���������"+selector.students.size(), Toast.LENGTH_SHORT).show();
							return;
						}
						// ��ʼ����
						call(count);
					}
				});
				builder.setNegativeButton("ȡ��", null);
				builder.show();
			}
		});
		
		Button btnList = (Button) findViewById(R.id.buttonList);
		btnList.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, CallListActivity.class);
				startActivity(intent);
			}
		});
	}
	private void initSelector() {
		// TODO �Զ����ɵķ������
		selector = new Selector();
		// ���ı��ļ��ж���ѧ�����������ö�ȡ�ļ��ı�׼����try..catch..finally
		InputStream fis = null;
		try{
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
	}
	private void initComponent() {
		// TODO �Զ����ɵķ������
		btn_ds1=(Button) findViewById(R.id.btn_ds1);
		btn_ds2=(Button) findViewById(R.id.btn_ds2);
		sysm=(Button) findViewById(R.id.btn_sysm);
	}
	private void call(int count) {
		// ��ѧ�����������ѡ��count��
		selector.randomSelect(count);
		// ��ѡ��ѧ���б�ת��ΪCallDetail�б�
		List<CallDetail> selected = new ArrayList<CallDetail>(selector.selected.size());
		for(Student stu : selector.selected) {
			CallDetail r = new CallDetail();
			r.studentId = stu.id;
			r.name = stu.name;
			r.sex = stu.sex;
			selected.add(r);
		}
		// ����CallDetailActivity��ʾCallDetail
		Intent intent = new Intent(MainActivity.this, CallDetailActivity.class);
		intent.putExtra("op", "new");	// �µ���
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timestamp = formater.format(new Date()); 
		intent.putExtra("timestamp", timestamp);
		intent.putExtra("data", (Serializable)selected);
		startActivity(intent);
	}
}
