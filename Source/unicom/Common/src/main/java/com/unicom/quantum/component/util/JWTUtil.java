package com.unicom.quantum.component.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {
    // 过期时间，12小时
    private static final long GRACE_TIME = 12*60*60*1000;//30 * 60 * 1000;
    //过期时间15天
    //private static final long GRACE_TIME = 15*24*60*60*1000;
    // 加密算法，name的取值需和header的alg取值一致
    private static Algorithm ALGORITHM_SM3 = HMACSM3Algorithm.getInstance("SM3");
    /**
     * jwt形式的Token签名方法，生成最终Token
     *
     * @param **username**
     * @param **password**
     * @return "Header.Payload.Signature"形式的Token，BASE64URL编码过的
     */
    public static String generateToken(String username) {
        try {
            // 设置过期时间
            long now = System.currentTimeMillis();
            Date exp = new Date(now + GRACE_TIME);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "SM3");
            // 返回token字符串
            return JWT.create()
                    .withHeader(header)
                    .withIssuer("qtec_R&D")
                    .withAudience(username)             // 接收该JWT的一方
                    .withSubject("cipher_server")      // 该JWT所面向的用户
                    .withExpiresAt(exp)               // 指定token的生命周期
                    .withIssuedAt(new Date(now))     // token创建时间
//                    .withJWTId(password)            // 针对当前token的唯一标识
                    .sign(ALGORITHM_SM3);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateTokenWithId(Integer id, String username) {
        try {
            // 设置过期时间
            long now = System.currentTimeMillis();
            Date exp = new Date(now + GRACE_TIME);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "SM3");
            // 返回token字符串
            return JWT.create()
                    .withHeader(header)
                    .withIssuer("qtec_R&D")
                    .withAudience(username)// 接收该JWT的一方
                    .withClaim("userId",id)
                    .withSubject("cipher_server")      // 该JWT所面向的用户
                    .withExpiresAt(exp)               // 指定token的生命周期
                    .withIssuedAt(new Date(now))     // token创建时间
//                    .withJWTId(password)            // 针对当前token的唯一标识
                    .sign(ALGORITHM_SM3);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String generateToken(String username,long time) {
        try {
            // 设置过期时间
            long now = System.currentTimeMillis();
            Date exp = new Date(now + time);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "SM3");
            // 返回token字符串
            return JWT.create()
                    .withHeader(header)
                    .withIssuer("qtec_R&D")
                    .withAudience(username)             // 接收该JWT的一方
                    .withSubject("cipher_server")      // 该JWT所面向的用户
                    .withExpiresAt(exp)               // 指定token的生命周期
                    .withIssuedAt(new Date(now))     // token创建时间
//                    .withJWTId(password)            // 针对当前token的唯一标识
                    .sign(ALGORITHM_SM3);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * jwt形式的Token验签方法，检验token是否正确
     *
     * @param token 待验证的token
     * @return
     */
    public static boolean verifyToken(String token,String username) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM_SM3).withAudience(username).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 获得token中的信息，无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) throws Exception{
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getAudience().get(0);
    }

    public static int getUserId(String token){
        DecodedJWT jwt = JWT.decode(token);
        Claim userId = jwt.getClaim("userId");
        return userId.asInt();
    }
    /**
     * 获得token中的到期时间信息
     *
     * @return token中包含的用户名
     */
    public static Date getExpiresAt(String token) throws Exception{
//        System.out.println(token);
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt();
    }
}
