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
			.getExternalStorageDirectory().getAbsolutePath();// 路径
//	public static final String SAVE_PATH_IN_SDCARD = "/daomeidan/"; // 图片及其他数据保存文件夹
//	public static final String IMAGE_CAPTURE_NAME = "cameraTmp.png"; // 照片名称
	public final static int REQUEST_CODE_TAKE_PICTURE = 12;// 设置拍照操作的标志
	protected static final int PICK_CONTACT = 2;
	/**联系人显示名称**/  
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;  
      
    /**电话号码**/  
    private static final int PHONES_NUMBER_INDEX = 1;  
      
    /**头像ID**/  
    private static final int PHONES_PHOTO_ID_INDEX = 2;  
     
    /**联系人的ID**/  
    private static final int PHONES_CONTACT_ID_INDEX = 3; 
	private String sdState = Environment.getExternalStorageState();// 获得sd卡的状态

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
				// 创建Intent
				Intent intent = new Intent();
				//设置Intent的Action属性
				intent.setAction(Intent.ACTION_GET_CONTENT);
				//设置Intent的Type属性
				intent.setType("vnd.android.cursor.item/phone");
				// 启动Activity，并希望获取该Activity的结果
				startActivityForResult(intent, 2);
			}
		});

		btnCamera = (Button) findViewById(R.id.button2);

		btnCamera.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 1);
				// 存储卡可用 将照片存储在 sdcard

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
					Toast.makeText(getApplicationContext(), "请完善信息",
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
	 * //////////获取照片 private void getImage(String imagePath){
	 * BitmapFactory.Options options = new BitmapFactory.Options();
	 * options.inJustDecodeBounds = true; // 获取这个图片的宽和高 bitmap =
	 * BitmapFactory.decodeFile(imagePath, options); //此时返回bm为空 //计算缩放比 int be =
	 * (int)(options.outHeight / (float)maxH); int ys = options.outHeight %
	 * maxH;//求余数 float fe = ys / (float)maxH; if(fe>=0.5)be = be + 1; if (be <=
	 * 0) be = 1; options.inSampleSize = be;
	 * 
	 * 
	 * //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
	 * options.inJustDecodeBounds = false;
	 * bitmap=BitmapFactory.decodeFile(imagePath,options);
	 * img.setImageBitmap(bitmap); // temp_person.setPhoto(bitmap);
	 * img.setVisibility(View.VISIBLE);
	 * 
	 * }
	 */
	// 获取拍照后图片数据
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.w("camera1", "success");
		
		if (resultCode == Activity.RESULT_OK) {
			Log.w("camera2", "success");
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.v("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			bitmap2 = null;
			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
			FileOutputStream b = null;
			Log.w("camera3", "success");
			File file = new File("/sdcard/myImage/");
			file.mkdirs();// 创建文件夹
			String fileName = "/sdcard/myImage/" + photo_id.toString() + ".jpg";

			try {
				b = new FileOutputStream(fileName);
				Bitmap.createScaledBitmap(bitmap, 200, 200, true);
				// bitmap2 =Bitmap.createBitmap(bitmap, 0, 0, 192, 192);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, 121, 121);
				Log.w("camera4", "success");
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
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

			img.setImageBitmap(bitmap);	// 将图片显示在ImageView里
	/*	switch (requestCode)
			{
				case (2):
					if (resultCode == Activity.RESULT_OK)
					{
						// 获取返回的数据
						Uri contactData = data.getData();
						// 查询联系人信息
						Cursor cursor = managedQuery(contactData, null
							, null, null, null);
						// 如果查询到指定的联系人
						if (cursor.moveToFirst())
						{
							String contactId = cursor.getString(cursor
								.getColumnIndex(ContactsContract.Contacts._ID));
							Long contactid= Long.parseLong(contactId);
							// 获取联系人的名字
							String name = cursor.getString(cursor
								.getColumnIndexOrThrow(
									ContactsContract.Contacts.DISPLAY_NAME));
							String phoneNumber = "此联系人暂未输入电话号码";
							System.out.println("-------------" + contactId);
							//根据联系人查询该联系人的详细信息
							Cursor phones = getContentResolver().query(
								ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
								null,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
							System.out.println("===================" + phones);
							if (phones.moveToFirst())
							{
								//取出电话号码
								phoneNumber = phones
									.getString(phones
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
								
								//得到联系人头像ID  
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
							// 关闭游标
							phones.close();
							EditText show = (EditText) findViewById(R.id.editText1);
							//显示联系人的名称
							show.setText(name);
						//	EditText phone = (EditText) findViewById(R.id.phone);
							//显示联系人的电话号码
						//	phone.setText(phoneNumber);
						}
						// 关闭游标
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
	 * 获得图片 图片高度 最大maxH
	 * 
	 * @param imagePath
	 */
	//

	// ///////以上是相机

}
