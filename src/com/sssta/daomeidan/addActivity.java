package com.sssta.daomeidan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class addActivity extends Activity {
	private Bitmap contactPhoto = null;
	private Button contact;
	private Button btnCamera;
	private Button btnok;
	private EditText name;
	private ImageView img;
	private Bitmap bitmap;
	private Boolean hasShootPic;
	private int maxH = 20;
	private Person temp_person;
	private integer num;
	private List<Person> PersonList;
	private Integer photo_id;
	private String str_photo_id;
	private Bitmap bitmap2;
	public static final String SDCARD_ROOT_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath();// ·��
//	public static final String SAVE_PATH_IN_SDCARD = "/daomeidan/"; // ͼƬ���������ݱ����ļ���
//	public static final String IMAGE_CAPTURE_NAME = "cameraTmp.png"; // ��Ƭ����
	public final static int REQUEST_CODE_TAKE_PICTURE = 12;// �������ղ����ı�־
	protected static final int PICK_CONTACT = 2;
	/**��ϵ����ʾ����**/  
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;  
      
    /**�绰����**/  
    private static final int PHONES_NUMBER_INDEX = 1;  
      
    /**ͷ��ID**/  
    private static final int PHONES_PHOTO_ID_INDEX = 2;  
     
    /**��ϵ�˵�ID**/  
    private static final int PHONES_CONTACT_ID_INDEX = 3; 
	private String sdState = Environment.getExternalStorageState();// ���sd����״̬

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.addactivity);

		name = (EditText) findViewById(R.id.editText1);
		img = (ImageView) findViewById(R.id.imageView1);
		temp_person = new Person();
		temp_person.setName(null);
		PersonList = new ArrayList<Person>();
		Intent intent2 = this.getIntent();
		Object[] cobjs = (Object[]) intent2.getSerializableExtra("PersonList");
		for (int i = 0; i < cobjs.length; i++) {
			Person Person = (Person) cobjs[i];
			PersonList.add(Person);
		}
		if (PersonList.size() == 0)
			photo_id = 1;
		else
			photo_id = PersonList.get(PersonList.size() - 1).getPhoto_id() + 1;
		// temp_person.setPhoto(img.getDrawingCache());

		contact = (Button) findViewById(R.id.button1);
		contact.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ����Intent
				Intent intent = new Intent();
				//����Intent��Action����
				intent.setAction(Intent.ACTION_GET_CONTENT);
				//����Intent��Type����
				intent.setType("vnd.android.cursor.item/phone");
				// ����Activity����ϣ����ȡ��Activity�Ľ��
				startActivityForResult(intent, 2);
			}
		});

		btnCamera = (Button) findViewById(R.id.button2);

		btnCamera.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 1);
				// �洢������ ����Ƭ�洢�� sdcard

				/*
				 * if (!sdState.equals(Environment.MEDIA_MOUNTED)){
				 * intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new
				 * File(SDCARD_ROOT_PATH+
				 * SAVE_PATH_IN_SDCARD,IMAGE_CAPTURE_NAME))); }
				 * startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
				 */
			}
		});

		btnok = (Button) findViewById(R.id.button_yes);
		btnok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (name.getText() == null)
					Toast.makeText(getApplicationContext(), "��������Ϣ",
						     Toast.LENGTH_SHORT).show();
				else {
					Intent intent = new Intent(addActivity.this,
							mainactivity.class);
					if (name.getText() != null) {
						temp_person.setName(name.getText().toString());
						temp_person.setPhoto_id(photo_id);
						PersonList.add(temp_person);
						intent.putExtra("PersonList2", PersonList.toArray());
						startActivity(intent);
						finish();
					}
				}
			}
		});

	}

	/*
	 * //////////��ȡ��Ƭ private void getImage(String imagePath){
	 * BitmapFactory.Options options = new BitmapFactory.Options();
	 * options.inJustDecodeBounds = true; // ��ȡ���ͼƬ�Ŀ�͸� bitmap =
	 * BitmapFactory.decodeFile(imagePath, options); //��ʱ����bmΪ�� //�������ű� int be =
	 * (int)(options.outHeight / (float)maxH); int ys = options.outHeight %
	 * maxH;//������ float fe = ys / (float)maxH; if(fe>=0.5)be = be + 1; if (be <=
	 * 0) be = 1; options.inSampleSize = be;
	 * 
	 * 
	 * //���¶���ͼƬ��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false
	 * options.inJustDecodeBounds = false;
	 * bitmap=BitmapFactory.decodeFile(imagePath,options);
	 * img.setImageBitmap(bitmap); // temp_person.setPhoto(bitmap);
	 * img.setVisibility(View.VISIBLE);
	 * 
	 * }
	 */
	// ��ȡ���պ�ͼƬ����
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.w("camera1", "success");
		
		if (resultCode == Activity.RESULT_OK) {
			Log.w("camera2", "success");
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����
				Log.v("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			bitmap2 = null;
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ
			FileOutputStream b = null;
			Log.w("camera3", "success");
			File file = new File("/sdcard/myImage/");
			file.mkdirs();// �����ļ���
			String fileName = "/sdcard/myImage/" + photo_id.toString() + ".jpg";

			try {
				b = new FileOutputStream(fileName);
				Bitmap.createScaledBitmap(bitmap, 200, 200, true);
				// bitmap2 =Bitmap.createBitmap(bitmap, 0, 0, 192, 192);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, 121, 121);
				Log.w("camera4", "success");
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			img.setImageBitmap(bitmap);	// ��ͼƬ��ʾ��ImageView��
	/*	switch (requestCode)
			{
				case (2):
					if (resultCode == Activity.RESULT_OK)
					{
						// ��ȡ���ص�����
						Uri contactData = data.getData();
						// ��ѯ��ϵ����Ϣ
						Cursor cursor = managedQuery(contactData, null
							, null, null, null);
						// �����ѯ��ָ������ϵ��
						if (cursor.moveToFirst())
						{
							String contactId = cursor.getString(cursor
								.getColumnIndex(ContactsContract.Contacts._ID));
							Long contactid= Long.parseLong(contactId);
							// ��ȡ��ϵ�˵�����
							String name = cursor.getString(cursor
								.getColumnIndexOrThrow(
									ContactsContract.Contacts.DISPLAY_NAME));
							String phoneNumber = "����ϵ����δ����绰����";
							System.out.println("-------------" + contactId);
							//������ϵ�˲�ѯ����ϵ�˵���ϸ��Ϣ
							Cursor phones = getContentResolver().query(
								ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
								null,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
							System.out.println("===================" + phones);
							if (phones.moveToFirst())
							{
								//ȡ���绰����
								phoneNumber = phones
									.getString(phones
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								
								//�õ���ϵ��ͷ��ID  
							    Long photoid = phones.getLong(PHONES_PHOTO_ID_INDEX);  
							  
							    if(photoid > 0 ) {  
							        Uri uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);  
							        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), uri);  
							        contactPhoto = BitmapFactory.decodeStream(input);  
							    }else {  
							        contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.def);  
							    }  
							    img.setImageBitmap(contactPhoto);
							}
							// �ر��α�
							phones.close();
							EditText show = (EditText) findViewById(R.id.editText1);
							//��ʾ��ϵ�˵�����
							show.setText(name);
						//	EditText phone = (EditText) findViewById(R.id.phone);
							//��ʾ��ϵ�˵ĵ绰����
						//	phone.setText(phoneNumber);
						}
						// �ر��α�
						cursor.close();
					}
					break;
			
		}  
		//    super.onActivityResult(requestCode, resultCode, data); */
		}
	}
	// super.onActivityResult(requestCode, resultCode, data);

	// ////
	/*
	 * ���ͼƬ ͼƬ�߶� ���maxH
	 * 
	 * @param imagePath
	 */
	//

	// ///////���������

}
