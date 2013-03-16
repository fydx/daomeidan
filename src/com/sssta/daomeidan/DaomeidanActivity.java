package com.sssta.daomeidan;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DaomeidanActivity extends Activity {
	/** Called when the activity is first created. */
	
	private Button button;
	private String path;
	private String str;
	final Intent intent2 = new Intent(DaomeidanActivity.this,
				mainactivity.class);
	Timer timer = new Timer();
	TimerTask task = new TimerTask() {
	   @Override
	   public void run() {
	    startActivity(intent2); //执行
	   }
	  };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		timer.schedule(task, 1000*1); //1秒后
		path = "/sdcard/myImage/";
		File f = new File(path);
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (f.exists()) {
			delFolder(path);
			boolean success = delAllFile(path);
			if (success == true)
				str = "success";
			else
				str = "failed";
		}
		Log.e("delete", str);
		
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DaomeidanActivity.this,
						mainactivity.class);
				startActivity(intent);
				// finish();
			}
		});

	}

	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onBackPressed() {
		System.exit(0);
	}
}
