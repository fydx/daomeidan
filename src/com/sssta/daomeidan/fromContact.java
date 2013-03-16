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
	private List<Map<String, Object>> mData; //listview�����ļ�
	private Context mContext = null;
	
	private static final String[] PHONES_PROJECTION = new String[] {  
	       Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID }; 
	
    /**��ϵ����ʾ����**/  
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;  
      
    /**�绰����**/  
    private static final int PHONES_NUMBER_INDEX = 1;  
      
    /**ͷ��ID**/  
    private static final int PHONES_PHOTO_ID_INDEX = 2;  
     
    /**��ϵ�˵�ID**/  
    private static final int PHONES_CONTACT_ID_INDEX = 3; 
    public static Map<Integer, Boolean> isSelected;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fromcontact);
	        mContext = this;  
	       
	          ListView list = (ListView)findViewById(R.id.listView1);       
	        /*
	         * �õ������ֻ���ϵ�˵�list
	         */
	         List<Person> contacts= new ArrayList<Person>();
	      
              ContentResolver resolver = mContext.getContentResolver();  
              
			// ��ȡ�ֻ���ϵ��  
			Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, "sort_key asc");
			Log.w("start", "success");
			startManagingCursor(phoneCursor);
				  
			if (phoneCursor != null) {  
				    while (phoneCursor.moveToNext()) {  
				    	
				    //�õ��ֻ�����  
				//    String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);  
				    //���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��  
				//    if (TextUtils.isEmpty(phoneNumber))  
				  //      continue;  
				    Log.e("start", "success");
				      
				    //�õ���ϵ������  
				//    String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);  
				    String contactName = phoneCursor.getString(phoneCursor
							.getColumnIndexOrThrow(
								ContactsContract.Contacts.DISPLAY_NAME));
				    
				    //�õ���ϵ��ID  
				    Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
				 //   String id = (String)contactid;
				  //  Log.d("id",contactid);  
				    //�õ���ϵ��ͷ��ID  
				    Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);  
				      
				    //�õ���ϵ��ͷ��Bitmap  
				    Bitmap contactPhoto = null;  
				    
				    Person temp_person = new Person();
				   
				    
				    //photoid ����0 ��ʾ��ϵ����ͷ�� ���û�и���������ͷ�������һ��Ĭ�ϵ�  
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
				///////////��ϵ�˶�ȡģ�����
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
			        SimpleAdapter mSchedule = new SimpleAdapter(this, // ûʲô����
							mData,// ������Դ
							R.layout.listitem,// ListItem��XMLʵ��

							// ��̬������ListItem��Ӧ������
							new String[] { "img", "name" },

							// ListItem��XML�ļ����������TextView ID
							new int[] { R.id.img, R.id.name});
					// ��Ӳ�����ʾ
					list.setAdapter(mSchedule);	 	    
	 }	 
}
