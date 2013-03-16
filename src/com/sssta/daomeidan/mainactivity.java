package com.sssta.daomeidan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class mainactivity extends Activity {
	private List<Map<String, Object>> Data;
	private Button button;
	private Button button2;
	private ListView listView;
	private Drawable def;
	private Bitmap bmdef;
	private List<Person> PersonList;// 用于rand函数的备选人列表
	private Person dmd_person;// 需要传给finalactivity的最终的person
	private List<Map<String, Object>> mData; // listview数据文件

	public Person rand(List<Person> personlist) {
		// int max = 0;

		Person max_num_person = new Person();
		for (int i = 0; i < personlist.size(); i++) {
			int num = (int) (Math.random() * 100 + Math.random() * 10);
			personlist.get(i).setNum(num);
			for (int k = 0; k < i; k++) {
				while (true) {
					if (personlist.get(i).getNum() == personlist.get(k)
							.getNum() && k != i) {
						num = (int) (Math.random() * 100 + Math.random() * 10);
						personlist.get(i).setNum(num);
						k = 0;
					} else
						break;
				}
			}
		}
		for (int i = 0; i < personlist.size(); i++) {
			if (max_num_person.getNum() < personlist.get(i).getNum())
				max_num_person = personlist.get(i);
		}
		return max_num_person;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);
		def = getResources().getDrawable(R.drawable.def);
		BitmapDrawable bd = (BitmapDrawable) def;
		bmdef = bd.getBitmap();
		listView = (ListView) findViewById(R.id.listView1);
		// Bundle bundle = this.getIntent().getExtras();
		// Person dmd = (Person)bundle.getSerializable("dmd_person");
		try {
			Intent intent = this.getIntent();
			// Object[] cobjs = (Object[])
			PersonList = null;
			PersonList = new ArrayList<Person>();
			Intent intent2 = this.getIntent();
			Object[] cobjs = (Object[]) intent2
					.getSerializableExtra("PersonList2");
			for (int i = 0; i < cobjs.length; i++) {
				Person Person = (Person) cobjs[i];
				PersonList.add(Person);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		mData = new ArrayList<Map<String, Object>>();

		try {
			for (Person Person : PersonList) {
				Log.e("start", "success");
				Map<String, Object> map = new HashMap<String, Object>();
				String Path_Photo = "/sdcard/myImage/"
						+ Person.getPhoto_id().toString() + ".jpg";
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				Bitmap bm = BitmapFactory.decodeFile(Path_Photo, options);
				Log.w("photoid", Person.getPhoto_id().toString());
				if (bm==null) {
					map.put("img", bmdef);
				}
				else {
					map.put("img", bm);
				}

				map.put("name", Person.getName());
				mData.add(map);
				Log.w("name", "111");

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		Log.e("start", "success");
		SimpleAdapter mSchedule = new ImageSimpleAdapter(this, // 没什么解释
				mData,// 数据来源
				R.layout.listitem,// ListItem的XML实现

				// 动态数组与ListItem对应的子项
				new String[] { "img", "name" },

				// ListItem的XML文件里面的两个TextView ID
				new int[] { R.id.img, R.id.name });
		// 添加并且显示
		listView.setAdapter(mSchedule);

		button = (Button) findViewById(R.id.button_add);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(mainactivity.this, addActivity.class);
				intent.putExtra("PersonList", PersonList.toArray());
				startActivity(intent);
				finish();
			}
		});
		button2 = (Button) findViewById(R.id.button_finish);
		button2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(PersonList.size()==0)
				{
					Toast.makeText(getApplicationContext(), "请添加倒霉蛋", Toast.LENGTH_SHORT).show();
				}
				else 
				{
					dmd_person = rand(PersonList);
					Intent intent = new Intent(mainactivity.this,
							finalactivity.class);
					intent.putExtra("dmd_person", dmd_person);
					startActivity(intent);
					finish();
				}
				
			}
		});

	}

	/*
	 * List<Person> persons = new ArrayList<Person>(); int k = 15; for( int i =
	 * 0; i<k;i++) {
	 * 
	 * persons.add(Person) }
	 */

	/*
	 * 按返回键确认退出模块
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle("确认返回吗？")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// 点击“确认”后的操作
						System.exit(0);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// 点击“返回”后的操作,这里不设置没有任何操作
					}
				}).show();
		// super.onBackPressed();

	}

}

// 显示SD卡里的图片缩略图
class ListViewBinder implements ViewBinder {

	public boolean setViewValue(View view, Object data,
			String textRepresentation) {
		// TODO Auto-generated method stub
		if ((view instanceof ImageView) && (data instanceof Bitmap)) {
			ImageView imageView = (ImageView) view;
			Bitmap bmp = (Bitmap) data;
			imageView.setImageBitmap(bmp);
			return true;
		}
		return false;
	}

}

class ImageSimpleAdapter extends SimpleAdapter {
	private int[] mTo;
	private String[] mFrom;
	private ViewBinder mViewBinder;
	private List<? extends Map<String, ?>> mData;
	private int mResource;
	private int mDropDownResource;
	private LayoutInflater mInflater;

	public ImageSimpleAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		mTo = to;
		mFrom = from;
		mData = data;
		mResource = mDropDownResource = resource;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mResource);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent,
				mDropDownResource);
	}

	private View createViewFromResource(int position, View convertView,
			ViewGroup parent, int resource) {
		View v;
		if (convertView == null) {
			v = mInflater.inflate(resource, parent, false);
		} else {
			v = convertView;
		}

		bindView(position, v);

		return v;
	}

	private void bindView(int position, View view) {
		final Map dataSet = mData.get(position);
		if (dataSet == null) {
			return;
		}

		final ViewBinder binder = mViewBinder;
		final String[] from = mFrom;
		final int[] to = mTo;
		final int count = to.length;

		for (int i = 0; i < count; i++) {
			final View v = view.findViewById(to[i]);
			if (v != null) {
				final Object data = dataSet.get(from[i]);
				String text = data == null ? "" : data.toString();
				if (text == null) {
					text = "";
				}

				boolean bound = false;
				if (binder != null) {
					bound = binder.setViewValue(v, data, text);
				}

				if (!bound) {
					if (v instanceof Checkable) {
						if (data instanceof Boolean) {
							((Checkable) v).setChecked((Boolean) data);
						} else if (v instanceof TextView) {
							// Note: keep the instanceof TextView check at the
							// bottom of these
							// ifs since a lot of views are TextViews (e.g.
							// CheckBoxes).
							setViewText((TextView) v, text);
						} else {
							throw new IllegalStateException(v.getClass()
									.getName()
									+ " should be bound to a Boolean, not a "
									+ (data == null ? "<unknown type>"
											: data.getClass()));
						}
					} else if (v instanceof TextView) {
						// Note: keep the instanceof TextView check at the
						// bottom of these
						// ifs since a lot of views are TextViews (e.g.
						// CheckBoxes).
						setViewText((TextView) v, text);
					} else if (v instanceof ImageView) {
						if (data instanceof Integer) {
							setViewImage((ImageView) v, (Integer) data);
						} else if (data instanceof Bitmap) {// 仅仅添加这一步
							setViewImage((ImageView) v, (Bitmap) data);
						} else {
							setViewImage((ImageView) v, text);
						}
					} else {
						throw new IllegalStateException(
								v.getClass().getName()
										+ " is not a view that can be bounds by this SimpleAdapter");
					}
				}
			}
		}
	}

	/**
	 * 添加这个方法来处理Bitmap类型参数
	 * 
	 * @param v
	 * @param bitmap
	 */
	public void setViewImage(ImageView v, Bitmap bitmap) {
		v.setImageBitmap(bitmap);
	}
}
