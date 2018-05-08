package com.xjj.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by XuJijun on 2018-05-03.
 *
 * 定制化jwt工具类
 *
 * @param <T> jwt payload 类型 （POJO类）
 */

public class JwtUtils<T> {
    private static ObjectMapper objectMapper = new ObjectMapper();

    private HmacUtils hmacSha256 = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, "hmacKey");
    private AesUtils aesUtils = new AesUtils("AES/ECB/PKCS5Padding", "aesKey");
    private Class<T> payloadClass;

    public JwtUtils(Class<T> payloadClass) {
        this.payloadClass = payloadClass;
    }

    public JwtUtils(String hmacKey, String aesKey, Class<T> payloadClass) {
        this.hmacSha256 = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, hmacKey);
        this.aesUtils = new AesUtils("AES/ECB/PKCS5Padding", aesKey);
        this.payloadClass = payloadClass;
    }

    /**
     * 根据payload生成jwt 字符串，格式：
     * AES(payload).HMAC_SHA256(AES(payload))
     */
    public String generateJwt(T payload) throws JsonProcessingException {
        // 拼装和加密 payload:
        String payloadStr = objectMapper.writeValueAsString(payload);
        String aesPayload = aesUtils.encrypt(payloadStr);
        System.out.println("aesPayload: " + aesPayload);

        // HMAC-SHA256 签名:
        String signature = hmacSha256.hmacHex(aesPayload);
        //System.out.println(hmac);

        // 拼装jwt：
        String jwt = aesPayload.concat(".").concat(signature);
        System.out.println("jwt: " + jwt);

        return jwt;
    }

    /**
     * 从jwt字符串中提取payload
     */
    public Optional<T> extractPayload(String jwt) throws IOException {
        String[] ss = jwt.split("\\.");
        if(ss.length < 2){
            return Optional.empty();
        }

        String aesPayload = ss[0];
        String signature = ss[1];

        if(StringUtils.isAnyBlank(aesPayload, signature)){
            return Optional.empty();
        }

        if(!signature.equals(hmacSha256.hmacHex(aesPayload))){ //签名不符
            return Optional.empty();
        }

        String payload = aesUtils.decrypt(aesPayload);
        System.out.println("decoded payload: " + payload);

        return Optional.ofNullable(objectMapper.readValue(payload, payloadClass));

    }

    public static void main(String[] args) throws Exception {
        JwtUtils<JwtPayload> jwtUtils = new JwtUtils<>(JwtPayload.class);

        // id为String, 没有roles
        JwtPayload<String> jwtPayload = new JwtPayload<>(1, "xu", System.currentTimeMillis()/1000 + 8*60*60);
        String jwt = jwtUtils.generateJwt(jwtPayload);
        Optional<JwtPayload> decodedPayload = jwtUtils.extractPayload(jwt);
        if(decodedPayload.isPresent()){
            System.out.println(objectMapper.writeValueAsString(decodedPayload.get()));
        }
//        decodedPayload.ifPresent(e -> System.out.println(objectMapper.writeValueAsString(e)));


        // id为int, 没有roles
        JwtPayload<Integer> jwtPayload2 = new JwtPayload<>(2, 12345, System.currentTimeMillis()/1000 + 24*60*60);
        String jwt2 = jwtUtils.generateJwt(jwtPayload2);
        decodedPayload = jwtUtils.extractPayload(jwt2);
        if(decodedPayload.isPresent()){
            System.out.println(objectMapper.writeValueAsString(decodedPayload.get()));
        }


        // id为int, 有roles
        JwtPayload<Integer> payload3 = new JwtPayload<>(3, 54321, Arrays.asList("ADMIN", "BROWSER"),
                System.currentTimeMillis()/1000 + 24*60*60);
        String jwt3 = jwtUtils.generateJwt(payload3);
        decodedPayload = jwtUtils.extractPayload(jwt3);
        if(decodedPayload.isPresent()){
            System.out.println(objectMapper.writeValueAsString(decodedPayload.get()));
        }
    }
}
