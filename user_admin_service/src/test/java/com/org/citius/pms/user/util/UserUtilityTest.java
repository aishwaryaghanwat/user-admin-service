package com.org.citius.pms.user.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserUtilityTest {

	@InjectMocks
	private Hmac512PasswordEncoder hMac512PasswordEncoder;

	@Test
	public void test_get_SHA_512_SecurePassword() {
		String passwordToHash = "Admin1234";
		String encryptedTestHashedPassword = "{SSHA-512}984e92eaf3e1b98f80829e95f705776024dd6396f3b03e6709229520e5489e5a670ff2547f9a72a1235e8063433500af9df21f26f4bc306819a02eacf3c8200c";
		String encryptedHashedPassword = "";
		encryptedHashedPassword = this.hMac512PasswordEncoder.encode(passwordToHash);
		assertTrue(encryptedHashedPassword.equals(encryptedTestHashedPassword));
	}

}
