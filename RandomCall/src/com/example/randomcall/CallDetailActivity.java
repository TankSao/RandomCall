package com.example.randomcall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import com.example.randomcall.bean.CallDetail;
import com.example.randomcall.util.DBHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CallDetailActivity extends Activity {
	String op;				// new: 新点名; edit:以往点名; 二者在保存时处理方式不同
	String timestamp;		// 点名时间
	List<CallDetail> data;	// 点名详细数据
	SimpleAdapter adapter;	// 用ListView显示详细数据，adapter负责为ListView提供数据
	int currentItem;		// 列表中当前点击到的项目
	int photo[]=new int[100];
	String photo1[]=new String[100];
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_detail);
		// 从Intent中取出传递过来的参数
		iniPhoto();
		Intent intent = getIntent();
		op = intent.getStringExtra("op");
		timestamp = intent.getStringExtra("timestamp");
		data = (List<CallDetail>)(intent.getSerializableExtra("data"));	// 列表数据可以序列化后保存在intent中，取出时反序列化

		// 构造adapter需要的Map类型的List
		final ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for(CallDetail r : data) {
		    Map<String, String> map = new HashMap<String, String>();
		    map.put("name", r.name + ", " + r.state);
		    map.put("info", r.studentId + ", " + r.sex);
		    /*String zpname=r.studentId+".jpg";
		    map.put("zp", zpname);*/
		    list.add(map);
		}
		adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2, new String[]{"name", "info"}, new int[]{android.R.id.text1, android.R.id.text2});

		// 设置界面中的控件内容
		final ListView lv = (ListView)findViewById(R.id.listViewCallDetail);
		lv.setAdapter(adapter);
		TextView tvTitle = (TextView) findViewById(R.id.textViewCallDetailTitle);
		tvTitle.setText(timestamp);
		
		lv.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				currentItem = pos;
				showRecord();	// 点击ListView中一个条目后，显示学生点名的详细信息，并可以选择点名状态
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO 自动生成的方法存根
				currentItem = pos;
				showPhoto();
				return true;
			}
		});
		
		Button btnCancel = (Button) findViewById(R.id.buttonCancel);
		btnCancel.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				finish();
			}
		});
		
		Button btnSave = (Button) findViewById(R.id.buttonSave);
		btnSave.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				if("new".equals(op)){	// 新的点名时，需要在call_list表插入一条记录，并把每条数据都需要插入到call_detail表
					DBHelper dbHelper = new DBHelper(CallDetailActivity.this);
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					db.beginTransaction();
					try{
						ContentValues cv = new ContentValues();
						cv.put("timestamp", timestamp);
						db.insert(DBHelper.callListTableName, null, cv);	// 插入一个记录到call_list表
						// 获取新插入到call_list表的记录的_id，用它作为后面想call_detail表中插入数据的callId
						Cursor cur = db.rawQuery("select LAST_INSERT_ROWID() from " + DBHelper.callListTableName, null);
						cur.moveToFirst();
						int callId = cur.getInt(0);
						cur.close();
						// 将详细记录中的每条数据插入到call_detail表
						for(CallDetail r : data) {
							cv.clear();
							cv.put("student_id", r.studentId);
							cv.put("name", r.name);
							cv.put("sex", r.sex);
							cv.put("state", r.state);
							cv.put("callId", callId);
							db.insert(DBHelper.callDetailTableName, null, cv);
						}
						db.setTransactionSuccessful();	// 设置事务成功标志
					} finally {
						db.endTransaction();
					}
					db.close();
				} else if("edit".equals(op)) {	// 修改以往点名时，只需把改动过的数据更新到call_detail表
					DBHelper dbHelper = new DBHelper(CallDetailActivity.this);
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					db.beginTransaction();
					try{
						ContentValues cv = new ContentValues();
						for(CallDetail r : data) {
							if(r.modified) {
								cv.clear();
								cv.put("state", r.state);
								// 更新数据时以_id为记录标识，该字段在ListView中并不显示
								db.update(DBHelper.callDetailTableName, cv, "_id=?", new String[]{Integer.toString(r._id)});
							}
						}
						db.setTransactionSuccessful();	// 设置事务成功标志
					} finally {
						db.endTransaction();
					}
					db.close();
				}
				finish();
			}
		});
	}
	private void iniPhoto() {
		// TODO 自动生成的方法存根
		for(int i=0;i<48;i++){
			if(i>9){
			photo1[i]="201318141"+i;
			}else{
				photo1[i]="2013181410"+i;
			}
		}
		for(int i=0;i<48;i++){
				if(i>9){
				photo1[i]="201318142"+i;
				}else{
					photo1[i]="2013181420"+i;
				}
		}
		for(int i=0;i<96;i++){
			Random r=new Random();
			int a=r.nextInt(5);
			System.out.println(a);
			switch (a) {
case 0:
	photo[i]=R.drawable.zp6;		
				break;
case 1:
	photo[i]=R.drawable.zp1;
	break;
case 2:
	photo[i]=R.drawable.zp2;
	break;
case 3:
	photo[i]=R.drawable.zp3;
	break;
case 4:
	photo[i]=R.drawable.zp4;
	break;
			default:
				photo[i]=R.drawable.zp5;
				break;
			}
		}
	}
	AlertDialog dialog1;  //当老师长时间选中学生信息时候显示该学生的照片
	private void showPhoto() {
		// TODO 自动生成的方法存根
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final CallDetail r = data.get(currentItem);
		String zpname=r.studentId;
		ImageView iv=new ImageView(this);
		int i=0;
		for(int x=0;x<photo1.length;x++){
			if(zpname.equals(photo1[x])){
				i=x;
			}
		}
		//Toast.makeText(CallDetailActivity.this, i+"ssss", Toast.LENGTH_LONG).show();
		iv.setBackgroundResource(photo[i]);
		builder.setView(iv);
		dialog1=builder.create();
		dialog1.show();
	}
	// dialog只能在所有设置完成后创建，同时又必须在OnClickListener内部类中使用，所以只能声明为Activity类的成员
	AlertDialog dialog;
	/**
	 * 用AlertDialog显示一个学生点名的详细信息，并可以修改其状态
	 */
	void showRecord(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final CallDetail r = data.get(currentItem);
		builder.setTitle(r.name + ", " + r.studentId);
		builder.setNegativeButton("关闭", null);
		int stateIndex = Arrays.binarySearch(CallDetail.STATE, r.state);
		if(stateIndex<0) stateIndex=0;
		// 采用单选列表形式的AlertDialog，当前选中索引为stateIndex
		builder.setSingleChoiceItems(CallDetail.STATE, stateIndex, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				// 根据用户选择修改后台数据
				r.state = CallDetail.STATE[arg1];
				r.modified = true;
				// 更新adapter中数据，并更新ListView显示
				Map<String, String> itemMap = (Map<String, String>)adapter.getItem(currentItem);
				itemMap.put("name", r.name + ", " + r.state);
				adapter.notifyDataSetChanged();
				// 关闭AlertDialog，采用dialog.show方法显示的Dialog可以用dialog.dismiss方法关闭
				dialog.dismiss();
			}
		});
		dialog = builder.create();	// dialog只能在所有设置都完成后创建
		dialog.show();
	}
}

