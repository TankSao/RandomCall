package com.example.randomcall.adapter;

import java.util.List;

import com.example.randomcall.R;
import com.example.randomcall.bean.Student;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{
private Context context;
private List<Student> list;
public MyAdapter(Context c,List<Student> l){
	context=c;
	list=l;
}
	public int getCount() {
		// TODO 自动生成的方法存根
		return list.size();
	}

	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		View view=View.inflate(context, R.layout.mdcell, null);
		Student f=list.get(position);
		TextView tv1=(TextView) view.findViewById(R.id.tv_class);
		TextView tv2=(TextView) view.findViewById(R.id.tv_name);
		TextView tv3=(TextView) view.findViewById(R.id.tv_sex);
		tv1.setText(f.id);
		tv2.setText(f.name);
		tv3.setText(f.sex);
		return view;
	}

}

