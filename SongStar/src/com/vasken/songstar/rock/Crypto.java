package com.vasken.songstar.rock;

import java.security.MessageDigest;

import com.vasken.util.Base64;

public class Crypto {
	public static String createHash(String seed, String cleartext) throws Exception {
		// Create MD5 Hash  
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");  

        // Hash the request 
        digest.update((cleartext+seed).getBytes());  
        byte firstHash[] = digest.digest();  
        
        // Hash again for good luck
        digest.update(firstHash);  
        byte finalHash[] = digest.digest();  
        
        return Base64.encodeBytes(finalHash, Base64.URL_SAFE);
	}
}
