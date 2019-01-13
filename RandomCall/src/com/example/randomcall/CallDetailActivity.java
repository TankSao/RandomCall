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
	String op;				// new: �µ���; edit:��������; �����ڱ���ʱ����ʽ��ͬ
	String timestamp;		// ����ʱ��
	List<CallDetail> data;	// ������ϸ����
	SimpleAdapter adapter;	// ��ListView��ʾ��ϸ���ݣ�adapter����ΪListView�ṩ����
	int currentItem;		// �б��е�ǰ���������Ŀ
	int photo[]=new int[100];
	String photo1[]=new String[100];
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_detail);
		// ��Intent��ȡ�����ݹ����Ĳ���
		iniPhoto();
		Intent intent = getIntent();
		op = intent.getStringExtra("op");
		timestamp = intent.getStringExtra("timestamp");
		data = (List<CallDetail>)(intent.getSerializableExtra("data"));	// �б����ݿ������л��󱣴���intent�У�ȡ��ʱ�����л�

		// ����adapter��Ҫ��Map���͵�List
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

		// ���ý����еĿؼ�����
		final ListView lv = (ListView)findViewById(R.id.listViewCallDetail);
		lv.setAdapter(adapter);
		TextView tvTitle = (TextView) findViewById(R.id.textViewCallDetailTitle);
		tvTitle.setText(timestamp);
		
		lv.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				currentItem = pos;
				showRecord();	// ���ListView��һ����Ŀ����ʾѧ����������ϸ��Ϣ��������ѡ�����״̬
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO �Զ����ɵķ������
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
				if("new".equals(op)){	// �µĵ���ʱ����Ҫ��call_list�����һ����¼������ÿ�����ݶ���Ҫ���뵽call_detail��
					DBHelper dbHelper = new DBHelper(CallDetailActivity.this);
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					db.beginTransaction();
					try{
						ContentValues cv = new ContentValues();
						cv.put("timestamp", timestamp);
						db.insert(DBHelper.callListTableName, null, cv);	// ����һ����¼��call_list��
						// ��ȡ�²��뵽call_list��ļ�¼��_id��������Ϊ������call_detail���в������ݵ�callId
						Cursor cur = db.rawQuery("select LAST_INSERT_ROWID() from " + DBHelper.callListTableName, null);
						cur.moveToFirst();
						int callId = cur.getInt(0);
						cur.close();
						// ����ϸ��¼�е�ÿ�����ݲ��뵽call_detail��
						for(CallDetail r : data) {
							cv.clear();
							cv.put("student_id", r.studentId);
							cv.put("name", r.name);
							cv.put("sex", r.sex);
							cv.put("state", r.state);
							cv.put("callId", callId);
							db.insert(DBHelper.callDetailTableName, null, cv);
						}
						db.setTransactionSuccessful();	// ��������ɹ���־
					} finally {
						db.endTransaction();
					}
					db.close();
				} else if("edit".equals(op)) {	// �޸���������ʱ��ֻ��ѸĶ��������ݸ��µ�call_detail��
					DBHelper dbHelper = new DBHelper(CallDetailActivity.this);
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					db.beginTransaction();
					try{
						ContentValues cv = new ContentValues();
						for(CallDetail r : data) {
							if(r.modified) {
								cv.clear();
								cv.put("state", r.state);
								// ��������ʱ��_idΪ��¼��ʶ�����ֶ���ListView�в�����ʾ
								db.update(DBHelper.callDetailTableName, cv, "_id=?", new String[]{Integer.toString(r._id)});
							}
						}
						db.setTransactionSuccessful();	// ��������ɹ���־
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
		// TODO �Զ����ɵķ������
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
	AlertDialog dialog1;  //����ʦ��ʱ��ѡ��ѧ����Ϣʱ����ʾ��ѧ������Ƭ
	private void showPhoto() {
		// TODO �Զ����ɵķ������
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
	// dialogֻ��������������ɺ󴴽���ͬʱ�ֱ�����OnClickListener�ڲ�����ʹ�ã�����ֻ������ΪActivity��ĳ�Ա
	AlertDialog dialog;
	/**
	 * ��AlertDialog��ʾһ��ѧ����������ϸ��Ϣ���������޸���״̬
	 */
	void showRecord(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final CallDetail r = data.get(currentItem);
		builder.setTitle(r.name + ", " + r.studentId);
		builder.setNegativeButton("�ر�", null);
		int stateIndex = Arrays.binarySearch(CallDetail.STATE, r.state);
		if(stateIndex<0) stateIndex=0;
		// ���õ�ѡ�б���ʽ��AlertDialog����ǰѡ������ΪstateIndex
		builder.setSingleChoiceItems(CallDetail.STATE, stateIndex, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				// �����û�ѡ���޸ĺ�̨����
				r.state = CallDetail.STATE[arg1];
				r.modified = true;
				// ����adapter�����ݣ�������ListView��ʾ
				Map<String, String> itemMap = (Map<String, String>)adapter.getItem(currentItem);
				itemMap.put("name", r.name + ", " + r.state);
				adapter.notifyDataSetChanged();
				// �ر�AlertDialog������dialog.show������ʾ��Dialog������dialog.dismiss�����ر�
				dialog.dismiss();
			}
		});
		dialog = builder.create();	// dialogֻ�����������ö���ɺ󴴽�
		dialog.show();
	}
}

