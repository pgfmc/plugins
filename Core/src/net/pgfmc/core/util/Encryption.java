package net.pgfmc.core.util;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import net.pgfmc.core.CoreMain;

public final class Encryption {
	
	private static final PublicKey getPublicKey()
	{
		return Encryption.readPublicKey(Path.of(CoreMain.plugin.getDataFolder() + File.separator + "server-public-key.der"));
	}
	
	private static final PrivateKey getPrivateKey()
	{
		return Encryption.readPrivateKey(Path.of(CoreMain.plugin.getDataFolder() + File.separator + "proxy-private-key.der"));
	}

	private static final PublicKey readPublicKey(final Path path)
	{
		try {
			final byte[] bytes = Files.readAllBytes(path);
		    final X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(bytes);
		    final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		    
		    return keyFactory.generatePublic(publicSpec);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

	private static final PrivateKey readPrivateKey(final Path path)
	{
		try {
			final byte[] bytes = Files.readAllBytes(path);
		    final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
		    final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		    
		    return keyFactory.generatePrivate(keySpec);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static final String encrypt(final String plaintext)
	{
		try {
			final Cipher cipher = Cipher.getInstance("RSA");
		    
		    cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
		    
		    final byte[] bytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
		    final String cyphertext = new String(bytes, StandardCharsets.UTF_8);
		    
		    return cyphertext;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

	public static final String decrypt(final String ciphertext)
	{
		try {
			final Cipher cipher = Cipher.getInstance("RSA");
		    
		    cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
		    
		    final byte[] bytes = cipher.doFinal(ciphertext.getBytes(StandardCharsets.UTF_8));
		    final String secret = new String(bytes, StandardCharsets.UTF_8);
		    
		    return secret;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

}
