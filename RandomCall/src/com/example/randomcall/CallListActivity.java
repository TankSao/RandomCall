package com.example.randomcall;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.randomcall.bean.Call;
import com.example.randomcall.bean.CallDetail;
import com.example.randomcall.util.DBHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CallListActivity extends Activity {
	ListView lv;
	List<Call> data;
	ArrayAdapter<Call> adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_list);
		
		data = new ArrayList<Call>();
		loadCallList(data);
		
		adapter = new ArrayAdapter<Call>(this, android.R.layout.simple_expandable_list_item_1, data);

		lv = (ListView) findViewById(R.id.listViewCallList);
		lv.setAdapter(adapter);
		registerForContextMenu(lv);
		lv.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				//Toast.makeText(SelectorActivity.this, "Pos=" + pos, Toast.LENGTH_LONG).show();
				Call c = data.get(pos);
				List<CallDetail> detail = new ArrayList<CallDetail>();
				loadDetail(detail, c.id);
				Intent intent = new Intent(CallListActivity.this, CallDetailActivity.class);
				intent.putExtra("op", "edit");
				intent.putExtra("timestamp", c.timestamp);
				intent.putExtra("data", (Serializable)detail);
				startActivity(intent);
			}
		});
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		menu.add(1, 1, 1, "删除");
	}
	
    @Override
    public boolean onContextItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case 1:
        	AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo(); 
        	int pos = menuInfo.position;
        	Call c = data.get(pos);
        	deleteCall(c.id);
        	data.remove(pos);
        	adapter.notifyDataSetChanged();
    		break;
    	default:
    		return super.onContextItemSelected(item);
    	}
    	return true;
    }	
	
    /**
     * 从call_list表中读出所有点名列表
     * @param calls 存放点名列表
     */
	void loadCallList(List<Call> calls){
		calls.clear();
		DBHelper dbHelper = new DBHelper(this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cur = db.rawQuery("select * from " + DBHelper.callListTableName, null);
		int idIndex = cur.getColumnIndex("_id");
		int timeIndex = cur.getColumnIndex("timestamp");
		while(cur.moveToNext()) {
			Call c = new Call();
			c.id = cur.getInt(idIndex);
			c.timestamp = cur.getString(timeIndex);
			calls.add(c);
		}
		cur.close();
		db.close();
	}
	
	/**
	 * 从call_detail表中读出某次点名的详细数据
	 * @param detail 存放详细数据列表
	 * @param callId 点名的id，与call_list表中的_id字段对应
	 */
	void loadDetail(List<CallDetail> detail, int callId) {
		DBHelper dbHelper = new DBHelper(this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		// 查询
		Cursor cur = db.rawQuery("select * from " + DBHelper.callDetailTableName + " where callId=" + callId, null);
		// 得到每个字段在查询结果中的位置编号
		int _idIndex = cur.getColumnIndex("_id");
		int idIndex = cur.getColumnIndex("student_id");
		int nameIndex = cur.getColumnIndex("name");
		int sexIndex = cur.getColumnIndex("sex");
		int stateIndex = cur.getColumnIndex("state");
		// 查询结果放入detail列表
		while(cur.moveToNext()) {
			CallDetail r = new CallDetail();
			r._id = cur.getInt(_idIndex);
			r.studentId = cur.getString(idIndex);
			r.name = cur.getString(nameIndex);
			r.sex = cur.getString(sexIndex);
			r.state = cur.getString(stateIndex);
			detail.add(r);
		}
		cur.close();
		db.close();
	}
	
	/**
	 * 删除某次点名
	 * @param callId 要删除的点名的id
	 */
	void deleteCall(int callId) {
		DBHelper dbHelper = new DBHelper(CallListActivity.this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try{
			// 删除该点名在call_detail表中的详细记录
			db.execSQL("delete from " + DBHelper.callDetailTableName + " where callId=" + callId);
			// 删除该点名在call_list表中的记录
			db.execSQL("delete from " + DBHelper.callListTableName + " where _id=" + callId);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		db.close();
	}
}

