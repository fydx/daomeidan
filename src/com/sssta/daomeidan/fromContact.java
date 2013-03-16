package com.sssta.daomeidan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.string;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class fromContact extends Activity {
	private List<Map<String, Object>> mData; //listview数据文件
	private Context mContext = null;
	
	private static final String[] PHONES_PROJECTION = new String[] {  
	       Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID }; 
	
    /**联系人显示名称**/  
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;  
      
    /**电话号码**/  
    private static final int PHONES_NUMBER_INDEX = 1;  
      
    /**头像ID**/  
    private static final int PHONES_PHOTO_ID_INDEX = 2;  
     
    /**联系人的ID**/  
    private static final int PHONES_CONTACT_ID_INDEX = 3; 
    public static Map<Integer, Boolean> isSelected;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fromcontact);
	        mContext = this;  
	       
	          ListView list = (ListView)findViewById(R.id.listView1);       
	        /*
	         * 得到含有手机联系人的list
	         */
	         List<Person> contacts= new ArrayList<Person>();
	      
              ContentResolver resolver = mContext.getContentResolver();  
              
			// 获取手机联系人  
			Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, "sort_key asc");
			Log.w("start", "success");
			startManagingCursor(phoneCursor);
				  
			if (phoneCursor != null) {  
				    while (phoneCursor.moveToNext()) {  
				    	
				    //得到手机号码  
				//    String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);  
				    //当手机号码为空的或者为空字段 跳过当前循环  
				//    if (TextUtils.isEmpty(phoneNumber))  
				  //      continue;  
				    Log.e("start", "success");
				      
				    //得到联系人名称  
				//    String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);  
				    String contactName = phoneCursor.getString(phoneCursor
							.getColumnIndexOrThrow(
								ContactsContract.Contacts.DISPLAY_NAME));
				    
				    //得到联系人ID  
				    Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
				 //   String id = (String)contactid;
				  //  Log.d("id",contactid);  
				    //得到联系人头像ID  
				    Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);  
				      
				    //得到联系人头像Bitmap  
				    Bitmap contactPhoto = null;  
				    
				    Person temp_person = new Person();
				   
				    
				    //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的  
				    if(photoid > 0 ) {  
				        Uri uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);  
				        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);  
				        contactPhoto = BitmapFactory.decodeStream(input);  
				    }else {  
				        contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.def);  
				    }  
				    
				   // mContactsName.add(contactName);  
				    //  mContactsNumber.add(phoneNumber);  
				    //    mContactsPhonto.add(contactPhoto);  
				      temp_person.setName(contactName);
				      temp_person.setId(contactid);
				  //  temp_person.setPhoto(contactPhoto);
				    contacts.add(temp_person);
				    }  
				  
				    phoneCursor.close();  
				
	 }
				///////////联系人读取模块结束
				mData = new ArrayList<Map<String,Object>>();
				for(Person Person : contacts )
				{
					Map<String, Object> map = new HashMap<String, Object>();  
		//			map.put("img", Person.getPhoto());
					map.put("name", Person.getName());
					mData.add(map);
					Log.w("name", Person.getName());
					
				}
				 isSelected = new HashMap<Integer, Boolean>();    
			        for (int i = 0; i < mData.size(); i++) {    
			            isSelected.put(i, false);    
			        }
			        SimpleAdapter mSchedule = new SimpleAdapter(this, // 没什么解释
							mData,// 数据来源
							R.layout.listitem,// ListItem的XML实现

							// 动态数组与ListItem对应的子项
							new String[] { "img", "name" },

							// ListItem的XML文件里面的两个TextView ID
							new int[] { R.id.img, R.id.name});
					// 添加并且显示
					list.setAdapter(mSchedule);	 	    
	 }	 
}
