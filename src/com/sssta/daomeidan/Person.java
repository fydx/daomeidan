package com.sssta.daomeidan;

import java.io.Serializable;

import android.R.integer;
import android.graphics.Bitmap;


public class Person implements Serializable{

		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;
		private String name;
//		private Bitmap photo;
		private Integer photo_id;
		
		public Integer getPhoto_id() {
			return photo_id;
		}
		public void setPhoto_id(Integer photo_id) {
			this.photo_id = photo_id;
		}
		private Long id;
///		public Bitmap getPhoto() {
//			return photo;
//		}
//		public void setPhoto(Bitmap photo) {
//			this.photo = photo;
//		}
		private int num;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Person()
		{
			this.name = "";
//			this.photo =null;
			this.num = 0;
			this.id= (long) 0;
			this.photo_id= 0;
		}
}
