package com.example.entity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

@SuppressLint("NewApi")
public class PersonModel {
	// ���˻�����Ϣ
	public static String name;
	public static String gender;
	public static String district;
	public static String signature;
	public static Bitmap avatar;
	public static String avatarLocalPathString;
	public static String weijin;
	public static String pension;

	// �����˺������Ϣ
	public static String phoneNumber;
	public static String email;
	public static String weibaoID;
	public static String password;
	public static boolean isPhoneNumberChecked;
	public static boolean isEmailChecked;
	
	//��¼�˱����û���Ϣ��preference������
	public static final String USER_PREF = "RegisteredUser";
	
	//PersonModel�޸�2.0 2013.12.1
	public static final int SIGNATURE_MAX_LENGTH = 140;
	
}