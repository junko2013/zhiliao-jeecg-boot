package org.jeecg.common.system.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.encryption.AesEncryptUtil;
import org.jeecg.common.util.oConvertUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 应用端jwt工具
 */
@Slf4j
public class JwtUtilApp {

	public static final long EXPIRE_TIME_ACCESS_TOKEN = 2*60*60 * 1000;//应用端access_token有效时长  2小时
	public static final long EXPIRE_TIME_REFRESH_TOKEN = 7*24*60*60 * 1000;//应用端refresh_token有效时长  7天
	public static final String secretKey = "rC5hBWN!yXUU#A#$"; //密钥

	/**
	 * 校验token是否正确
	 *
	 * @param token  密钥
	 * @return 是否正确
	 */
	public static Integer verify(String token) {
		try {
			// 根据密钥生成JWT效验器
			JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secretKey)).build();
			// 效验TOKEN
			DecodedJWT jwt = verifier.verify(token);
			if(jwt.getClaim("userId")==null){
				return null;
			}
			return jwt.getClaim("userId").asInt();
		} catch (Exception e) {
			log.error("verify jwt error:{}",e.getMessage());
			return null;
		}
	}

	public static void main(String[] args) {
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJwYXNzd29yZCI6IndqQ0JMU09YSHZBU0FTWEZ4cVR4QmcwenI1aTBoMFNKeHZTL0pwS2haSUxaYlZybjlUNlFGQXZ5QTJENXdlT24iLCJleHAiOjE3MDkxMTU0NjksInVzZXJJZCI6MTU1LCJhY2NvdW50IjoiNjY2In0.O3Ln3g5pVtmm83qUuWmpMX03dP7C9pjDAofnuQw3ALRtDmq1fMxmw4hBySNEbBmI0S7rMk3-DQl1a-6eEAOA0g";
		int userId = verify(token);
		System.out.println(userId);
	}

	/**
	 * 应用端token
	 * @param userId
	 * @param password
	 * @return
	 */
	public static String getAccessToken(Integer userId, String password, String account) {
		Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME_ACCESS_TOKEN);
		return JWT.create()
				.withClaim("userId", userId)
				.withClaim("password", AesEncryptUtil.encrypt(password))
				.withClaim("account", account)
				.withExpiresAt(expirationDate)
				.sign(Algorithm.HMAC512(secretKey));
	}
	/**
	 * 应用端token
	 * @param userId
	 * @param password
	 * @return
	 */
	public static String getRefreshToken(Integer userId, String password) {
		Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME_REFRESH_TOKEN);
		return JWT.create()
				.withClaim("userId", userId)
				.withClaim("password", AesEncryptUtil.encrypt(password))
				.withExpiresAt(date)
				.sign(Algorithm.HMAC512(secretKey));
	}

	/**
	 * 根据request中的token获取用户账号
	 * 
	 * @param request
	 * @return
	 * @throws JeecgBootException
	 */
	public static Integer getUserId(HttpServletRequest request) throws JeecgBootException {
		String authHeader = request.getHeader(CommonConstant.AUTHORIZATION);
		Integer userId = null;
		if (!oConvertUtils.isEmpty(authHeader)&&authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			userId = verify(token);
			if (userId == null) {
				throw new JeecgBootException("未获取到用户");
			}
		}
		return userId;
	}
	
}
