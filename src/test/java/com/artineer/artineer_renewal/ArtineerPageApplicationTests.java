package com.artineer.artineer_renewal;

import com.artineer.artineer_renewal.entity.User;
import com.artineer.artineer_renewal.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ArtineerPageApplicationTests {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	WebApplicationContext context;



	@Test
	void contextLoads() {
	}

	@Test
	void dummyDB() {
		for(int i=1; i<=180; i++) {
			// 년, 월, 일 범위를 조정하여 유효한 날짜로 변환
			int year = 1990 + (i % 21); // 1990 ~ 2010 사이의 년도
			int month = (i % 12) + 1;   // 1 ~ 12 사이의 월
			int day = (i % 28) + 1;     // 1 ~ 28 사이의 일 (모든 월에서 유효)

			// LocalDate를 사용해 유효한 날짜 생성
			LocalDate birthDate = LocalDate.of(year, month, day);

			// LocalDate를 yyyy/MM/dd 형식의 문자열로 변환
			String formattedDate = birthDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

			User user = new User(
					null,
					"id" + i,
					passwordEncoder.encode("pw" + i),
					"name" + i,
					i*i%4==0 || i%9 ==0 ? "male" : "female",
					formattedDate,  // 생년월일을 yyyy/MM/dd 형식으로 설정
					String.format("010-%04d-%04d", (1000 + i), (1000 + i)),
					"test" + i + "@arti.org",
					"zipcode" + i,
					"roadAddress" + i,
					"detailAddress" + i,
					(i % 10),
					"ROLE_GUEST",
					"1999/02/14 (12:44)",
					"1.2.3.4"
			);

			userRepository.save(user);
		}
	}


	@Test
	void MD5WithoutSalt() throws NoSuchAlgorithmException {
//		String pw = passwordEncoder.encode("12qwaszx");
		String input = "12qwaszx";

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(input.getBytes());
		byte[] digest = md.digest();

		// 바이트 배열을 16진수 문자열로 변환
		StringBuilder sb = new StringBuilder();
		for (byte b : digest) {
			sb.append(String.format("%02x", b));
		}

		System.out.println(sb.toString());
	}






	@Test
	void passwordEncoderChangeTest() throws Exception {
		//회원가입
//		User user = new User(
//				null,
//				"getUsername",
//				"5f7165fd2f3da538f8d38d3244f863f8", // imymemine
//				".getName()",
//				"malef",
//				"fordBirth",
//				".getTel()",
//				".getEmail()",
//				"125",
//				".getRoadAddress()",
//				".getDetailAddress()",
//				3,
//				"ROLE_GUEST",
//				"formattedDate",
//				"clientIp"
//		);

//		userRepository.save(user);


		String username = "getUsername";
		String password = "imymemine";
		System.out.println("username: " + username);
		System.out.println("password: " + password);

		mockMvc.perform(formLogin("/authenticate").user(username).password(password))
				.andDo(print())
				.andExpect(authenticated())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"));
	}

}
