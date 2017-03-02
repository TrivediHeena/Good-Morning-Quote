package com.trivediheena.goodmorningquote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper {
	public static final String KEYROW_ID="_id";
	public static final String KEY_QUOTE="quote";
	private static final String DB_NM="rand_quote";
	private static final String DB_TBL="quote";
	private static final int DB_VERSION=1;
	//public static final Uri url=Uri.parse("content://media/");
	private Helper helper;
	private final Context context;
	private SQLiteDatabase db;
	private static class Helper extends SQLiteOpenHelper{

		public Helper(Context context) {
			super(context, DB_NM, null, DB_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			String str="create table if not exists "+DB_TBL+" ("+KEYROW_ID+" integer primary key autoincrement,"+KEY_QUOTE+" varchar not null);";
			db.execSQL(str);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			String sql="drop table if exists "+DB_TBL;
			db.execSQL(sql);
			onCreate(db);
		}
	}
	public DBHelper(Context cont){
		context = cont;
	}
	public DBHelper open()throws SQLException{
		helper=new Helper(context);
		db=helper.getWritableDatabase();
		return this;
	}
	public void close()throws SQLException{
		helper.close();
	}
	public long createEntry(String nm)throws SQLException{
		ContentValues cv=new ContentValues();
		//cv.put(HelperCl.id,null);
		cv.put(KEY_QUOTE, nm);		
		return db.insert(DB_TBL, null, cv);
	}
	/*public long createEntry(String text) {
		// TODO Auto-generated method stub
		ContentValues cv=new ContentValues();
		cv.put(DBHelper.KEY_QUOTE, text);
		//cv.put(KEY_CONT, contact);		
		//cv.put(KEY_IMG, );
		return db.insert(DB_TBL, null, cv);
	}*/
	public List<String> select()throws SQLException{
		String[] cols={"_id","quote"};
		Cursor c=db.query(DB_TBL, cols, null, null, null, null,null);
		String res="";List<String> ar1=new ArrayList<String>();
		while(c.moveToNext())
		{
			//res=res+c.getString(0)+" "+c.getString(1)+" "+"\n";
			//res=c.getInt(0)+" "+c.getString(1);
			res=c.getString(1);
			ar1.add(res);
		}		
		c.close();
		return ar1;
	}
	public void deleteEntry(long id)throws SQLException{
		db.delete(DB_TBL, DBHelper.KEYROW_ID + "=" + id, null);
	}
	public long getId(long id,String item)throws SQLException{
		String[] cols={"_id","quote"};
		Cursor c=db.query(DB_TBL, cols, DBHelper.KEYROW_ID+"="+id+" and "+ DBHelper.KEY_QUOTE+"='"+item+"'",null,null,null,null);
		String res="";long idv=id;//List<String> ar1=new ArrayList<String>();
		//c.moveToFirst();
		while(c.moveToNext())
		{
			//res=res+c.getString(0)+" "+c.getString(1)+" "+"\n";
			//res=c.getInt(0)+" "+c.getString(1);
			//ar1.add(res);
			idv=c.getInt(0);
		}
		c.close();
		return id;
	}
	public Cursor all()throws SQLException{
		String[] cols=new String[]{"_id","quote"};
		Cursor c=db.query(DB_TBL, cols, null, null, null, null,null);
		c.moveToFirst();
		return c;
	}
}
