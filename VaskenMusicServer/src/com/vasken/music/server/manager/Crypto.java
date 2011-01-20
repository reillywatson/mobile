package com.vasken.music.server.manager;

import java.security.MessageDigest;

import com.vasken.music.server.util.Base64;

public class Crypto {
	public static boolean verifyHash(String privateKey, String data, String base64Hash) throws Exception {
		// Create MD5 Hash  
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");  

        // Hash the request 
        digest.update((data+privateKey).getBytes());  
        byte firstHash[] = digest.digest();  
        
        // Hash again for good luck
        digest.update(firstHash);  
        byte finalHash[] = digest.digest();  
        
        if (!Base64.encodeBytes(finalHash, Base64.URL_SAFE).equals(base64Hash))
        	throw new Exception("Decryption failed");
        
        return true;
	}
}
