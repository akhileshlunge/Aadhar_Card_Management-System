package com.aadhar.app.utility;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import com.aadhar.app.model.AadharCard;
import com.aadhar.app.model.User;

public class CommonUtility {
	
	public static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	
	public static String generateRandom(int length) {
		Random random = new Random();
		char[] digits = new char[length];
		digits[0] = (char) (random.nextInt(9) + '1');
		for (int i = 1; i < length; i++) {
			digits[i] = (char) (random.nextInt(10) + '0');
		}
		return new String(digits);
	}

	public static Blob generateBlobFromMultipartFile(MultipartFile multipartFile) {
		Blob blob = null;
		try {
			byte[] byteArr = multipartFile.getBytes();
			blob = new SerialBlob(byteArr);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
		return blob;
	}
	
	public static Date generateCurrentDateWithTimeStamp() {
		return new Date();
	}
	
	public static Date generateCurrentDateAfterOneWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.WEEK_OF_YEAR, 1);		
		return calendar.getTime();
	}
	
	public static String generateVerificationToken() {
		return UUID.randomUUID().toString();
		
	}

}
