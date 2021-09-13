package com.org.citius.pms.user.util;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Hmac512PasswordEncoder implements PasswordEncoder {

	private static final String HMAC_SHA512 = "HmacSHA512";

	public static final String SSHA512_PREFIX = "{SSHA-512}";

	private final String SALT = "CITIUS_512_ENCRYPT";

	public String encode(CharSequence rawPassword) {
		String result = null;

		try {
			Mac sha512Hmac = Mac.getInstance(HMAC_SHA512);
			final byte[] byteKey = Utf8.encode(SALT);
			SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
			sha512Hmac.init(keySpec);
			byte[] macData = sha512Hmac.doFinal(Utf8.encode(rawPassword.toString()));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < macData.length; i++) {
				sb.append(Integer.toString((macData[i] & 0xff) + 0x100, 16).substring(1));
			}
			result = SSHA512_PREFIX + sb.toString();
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (rawPassword == null || encodedPassword == null) {
			return false;
		}
		String encodedRawPass = encode(rawPassword);
		encodedRawPass = encodedRawPass.replace(SSHA512_PREFIX, "");
		return MessageDigest.isEqual(Utf8.encode(encodedRawPass), Utf8.encode(encodedPassword));
	}
}
