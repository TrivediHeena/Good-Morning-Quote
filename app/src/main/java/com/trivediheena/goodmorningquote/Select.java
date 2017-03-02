package com.trivediheena.goodmorningquote;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Select extends AppCompatActivity implements OnItemClickListener,OnItemLongClickListener{

	ArrayList<String> arl;
	ArrayAdapter<String> ad;
	ListView lv1;
	LinearLayout li;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select);
		lv1=(ListView)findViewById(R.id.lstSel);
		arl=new ArrayList<String>();
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		//ActionBar ab=getActionBar();
		LinearLayout li=(LinearLayout)findViewById(R.id.linear);
		//ab.setDisplayHomeAsUpEnabled(true);lv1.setLongClickable(true);
		DBHelper h=new DBHelper(this);
		h.open();
		//String result=h.select();
		arl.addAll(h.select());
		//arl.add(result);
		ad=new ArrayAdapter<String>(getApplicationContext(), R.layout.list, arl);
		lv1.setAdapter(ad);		
		h.close();
		lv1.setOnItemClickListener(this);
		lv1.setOnItemLongClickListener(this);		

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent=new Intent(getApplicationContext(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		//intent.setPackage("com.whatsapp");
		intent.putExtra(Intent.EXTRA_TEXT, arg0.getItemAtPosition(arg2).toString());
		startActivity(Intent.createChooser(intent, "Share With"));
	//	Toast.makeText(getApplicationContext(),lv1.getItemAtPosition(arg2), Toast.LENGTH_LONG).show();
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> lstAda, View arg1, int arg2,long arg3) {
		// TODO Auto-generated method stub
		DBHelper helper=new DBHelper(Select.this);
		helper.open();
		Cursor cursor=helper.all();
		cursor.moveToPosition(arg2);
		final long id1=cursor.getInt(0);
		final String itemData=cursor.getString(1);
		final String item1=lv1.getItemAtPosition(arg2).toString();
		final String adItem=ad.getItem(arg2);
		helper.close();
		Builder dialog=new Builder(this);
		dialog.setTitle("Delete Quote").setMessage("Are you sure you want to delete the quote?");
		dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				try{
					DBHelper h=new DBHelper(Select.this);
					h.open();
					if(itemData.equals(item1) && itemData.equals(adItem)){
					h.deleteEntry(id1);
					h.close();
					startActivity(new Intent(Select.this, Select.class));
					/*ad.remove(adItem);//ad.notifyDataSetChanged();
					ad.notifyDataSetChanged();*/
					}
					Toast.makeText(getApplicationContext(), "Quote Deleted Successfully", Toast.LENGTH_LONG).show();
					//startActivity(new Intent(getApplicationContext(), RandomMorningActivity.class));
				}
				catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getApplicationContext(), "Exception:Can't Delete Data", Toast.LENGTH_LONG).show();
				}
			}
		});
		dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
			}
		});
		dialog.show();
		//Toast.makeText(getApplicationContext(), "Long Click", Toast.LENGTH_SHORT).show();
		return true;
	}	
}