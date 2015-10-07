package com.consciencemobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	
	public static String MD5Password(String password){
		try {
			//�õ�һ����Ϣ������
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(password.getBytes());
			//System.out.println(result);
			StringBuffer buffer = new StringBuffer();
			
			//��ÿ��byte������������   0xff
			for(byte b :result){
				int number = b & 0xff;
				String str = Integer.toHexString(number);
				if(str.length() == 1){
					buffer.append("0");
				}
				buffer.append(str);
			}	
			
			return buffer.toString();
			
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	
		
	}
}
