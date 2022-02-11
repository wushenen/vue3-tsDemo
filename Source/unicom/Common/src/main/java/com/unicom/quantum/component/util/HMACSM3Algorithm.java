package com.unicom.quantum.component.util;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.IOException;

/**
 * Created by wuzh on 2020/1/14.
 * Describe: 采用SM3算法实现的jwt的第三部分的签证信息
 */
public class HMACSM3Algorithm extends Algorithm {
    private static final String DESCRIPTION = "HMACSM3";
    private static HMACSM3Algorithm THIS;

    private HMACSM3Algorithm(String name) {
        super(name, DESCRIPTION);
    }

    public static HMACSM3Algorithm getInstance(String name) {
        if (THIS == null) THIS = new HMACSM3Algorithm(name);
        return THIS;
    }

    // 验证签名
    @Override
    public void verify(DecodedJWT jwt) throws SignatureVerificationException {
        try {
            byte[] contentBytes = String.format("%s.%s", jwt.getHeader(), jwt.getPayload()).getBytes("UTF-8");
            boolean valid = Base64.encodeBase64URLSafeString(SM3Util.hash(contentBytes)).equals(jwt.getSignature());
            if (!valid) {
                throw new SignatureVerificationException(this);
            }
        } catch (IOException e) {
            throw new SignatureGenerationException(this, e);
        }
    }

    /**
     * @param bytes "Header(BASE64URL编码).Payload(BASE64URL编码)"的byte数组形式
     * @return "Header.Payload.Signature"的Signature部分
     * @throws SignatureGenerationException
     */
    @Override
    public byte[] sign(byte[] bytes) throws SignatureGenerationException {
        try {
            return SM3Util.hash(bytes);
        } catch (Exception e) {
            throw new SignatureGenerationException(this, e);
        }
    }
}
