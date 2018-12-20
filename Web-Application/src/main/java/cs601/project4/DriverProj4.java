package cs601.project4;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DriverProj4 {
	
	private static Logger logger = LogManager.getLogger();


	public static void main(String[] args) {
		
		
		String password = "orange";
		byte[] hashedPassword = hashPassword(password);
		
		System.out.println(new String(hashedPassword));
		System.out.println(new String(hashedPassword, StandardCharsets.UTF_8));

	}
	
	private static byte[] hashPassword(String password) {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		
		byte[] hashedPassword = null;
		try {
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			
			hashedPassword = factory.generateSecret(spec).getEncoded();
			
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		return hashedPassword;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
