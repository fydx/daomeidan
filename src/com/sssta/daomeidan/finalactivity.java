package com.sssta.daomeidan;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class finalactivity extends Activity {
	private TextView textview1;
	private ImageView dmdImageView;
	private Person Person_Final;
	private String path;
	private Bitmap bitmap;
	private Button again;
	

	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finalactivity);
		again=(Button) findViewById(R.id.again);
		again.setOnClickListener(new OnClickListener(
				) {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(finalactivity.this, DaomeidanActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//ͨ��setFlags��������activity��kill��
				startActivity(intent);
			}
		});
		
		bitmap=null;
		textview1 = (TextView) findViewById(R.id.textView_name);
		dmdImageView = (ImageView) findViewById(R.id.imageView_dmd);
		// Bundle bundle = this.getIntent().getExtras();
		// Person dmd = (Person)bundle.getSerializable("dmd_person");
		// textview1.setText(dmd.getName());
		// dmdImageView.setImageBitmap(dmd.getPhoto());
		Intent intent = this.getIntent();
		Person_Final = (Person) intent.getSerializableExtra("dmd_person");
		textview1.setText(Person_Final.getName());
		Log.w("final1", "success");
		path="/sdcard/myImage/"+Person_Final.getPhoto_id().toString()+".jpg";
		Log.w("final2", "success");
		bitmap=convertToBitmap(path, 192, 192);
		Log.w("final3", "success");
		dmdImageView.setImageBitmap(bitmap);

	}
	public Bitmap convertToBitmap(String path, int w, int h) {

		BitmapFactory.Options opts = new BitmapFactory.Options();
		// ����Ϊtureֻ��ȡͼƬ��С
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// ����Ϊ��
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// ����
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int) scale;
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
				BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}

}
