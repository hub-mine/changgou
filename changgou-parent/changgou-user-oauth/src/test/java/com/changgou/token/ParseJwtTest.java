package com.changgou.token;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

/*****
 * @Author: shenkunlin
 * @Date: 2019/7/7 13:48
 * @Description: com.changgou.token
 *  使用公钥解密令牌数据
 ****/
public class ParseJwtTest {

    /***
     * 校验令牌
     */
    @Test
    public void testParseToken(){
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJpdGhlaW1hIiwiaWQiOiIxIn0.cnz_NkUHdfYgLEMIWNVdgtpzUxcjPjqc2AnTHha79JzVemhN-raGZ1sajFU-Dj4rMZfH8MdFcJUsEpouxg5mE1auYmyPpEDkDztbcbkumfVoIDqnKVBBakupTtngXOhwoAM9nKAjqkf9Lt5nD3KDCIiKJmUlRWE1v4gXRz-q3sqOIVslbYk0LCuHxQKTuPSQ9ewwoNwHEy0zmW3XW_RKsXJvKpsz-TgUGKImvCiQj8Xm7iX7f39muM9tqmNSDHPu4836npMk9NIkUzeXjdu1tGiBilXfOqIyrAL4AgSOcvKVWY7BvwySUqSeqneHWzHAPNPI4EdVw7lgcNrsli0CKQ";

        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlHUiHJM5k59FeyyjP8UdMpfJWKd/d2wxLlih9sACDrutVHeV12YxPaf40igBokhcBnbbHxty7wGjk6YLRiO5A/zVgYfjUq1b1u2dt9VBWJB6LR+tZWdPRxp8cc0K1siou9D1INc9kOrbkGLM21LYx/JJA70JwocGeBmx8Y9r61p/b6Eqd6l6wUSXOMIpn8EkBYGyM7yWQs0ZGArAlxvA2zyXLZurTFqV1OixI4+exZVrXjnqh7muoOzn1CYu7zVgkjuqfEenyZMzWVfbOe+eBpB0OuOrD2jFIRbLyFZ+Q4zQrDkfUq+J8uJ1u8t3W5KZQWs5vmThNNDX27y0ZMM7wwIDAQAB-----END PUBLIC KEY-----";

        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));

        //获取Jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
