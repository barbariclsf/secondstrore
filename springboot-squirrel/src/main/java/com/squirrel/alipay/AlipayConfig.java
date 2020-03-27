package com.squirrel.alipay;

 /*
 * 支付宝配置类*/

import java.io.FileWriter;
import java.io.IOException;

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您
    public static String app_id = "2016101900722476";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key ="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCA/5xi19iJMi9omNqmNIsT8uk9Onny5QRhmXcjuE/fGHIj+bW5vZ4eKJnLfBA4o/JRwuhN13ojb+ZC0CzmWMRTwc9IZlyvJPJgy4C61bH16wmrsnbaR8fwPsOCJqnJVe243N3/GMDeimwoETst4+m8M2nSB24P2wkPWfdh43qnkSoLhZlPGHN4badKnJ/wJVXVy9AmL/el5sY+1H+nyYSM589MWhWmXB2OBrSZ1EWEeCCHtdtPp0uANxecO9yOW7wZJrtlJY/I1WpqqyMFYymi/KbXF4aarkeq0K8bGakrk+cajM0V1YV8n0iJ4b2vu4Ty0rS41NNZcI8CEsC5mcmLAgMBAAECggEAMftYa8/4PYhtaBi5t2ZqcOtQxgGPWLa10N82k2eJwwZky12otPQsfMD8fiRu4JNdg6KCgg64a1yjGkYw73t7bnyMxR+sT8yAAVM4aKv+DSvpjlZSyNbBOGwJMreqV7Fsr5EcjwfXSyLVL10l0e0aFmrRWKi6U3hp9UHFoW8TijMcrDGcycG69liAeKN9GwiMWnoTX3d34qmlsbQ/B466zowplkfL0tZQyCyg/I+bUVrkxTvmA/REBbSR5BiKlWEKfzS6HKWoqKZSj31GPWkE1vvqtRciW/07HNIucms8QyjxzZ2Um+iTtufcSafOgWXbWiQV++ZoiF1ngjjBzbCYYQKBgQDaFUFoEwx8DbKwBpRkKNK9gGIjikRzOIMkHo2oe/MWhC62itEOViM5k43qJI6qGQSXEhQYgYdMiiWlRCAv//O+HNgT7kD17y9yX4jhZjUKI1ZoQs3nb4uv4oaXxnT3lPmERA9hdQAtcmpL8zCcYnwmoomqGpqEGnCCT5wvDJzKOwKBgQCXbUFzSPIj9M/tXUGrpeAqeZ8MHau1M0tK/OvjQs5FK78dL/l05KGADjenDMVfFQzyNxwRQtNoKYIXbrJI+179lwQWifB0KZCQUQ+WjTuCP0BIeBnr6vhrvuu0i732EWyTdye8PsdVmvjHJCzMZzlVBLrhdyhuP8HG/01Dyp+48QKBgQC0wBU72z3nu7q0Jl/rO+Ke6b9tUXY2QUqAL0qdsE3kBZ5p8VggF8iOOfvPaolL9fxV8cfyH7kx+PYGqhY7clpk4ac9raTtsVaT5xaeKoYfZvaA542dGlieaJPBS906LqE3vOk6jyycfk4XsPJDS2U26FRNRkt23isN9kmuvTdebQKBgHgCmvCnS2C70V6pnjZPjE2dfjO4ADHnqTCRKtI+TAP+G8UiFG5UFLrMicbXFEW7BTuh+Zikga/31aaEkL/OmQYZ/Z9vSyaFGpGF7PjAjf0369aDeOfEnYWnFl9MsQyn2ok00kUa7BgobipFvUtyM7nxubqfGWfnF8iC5dVobRXBAoGAX0B+WVZOu5RIdKN41vMHHN4YbAZ6LdKw53Tr/A37787Mvy023FkBk+GzWLCfQHtcF71hM/OT9+JsCxxyKmvj/BbigPAD1df3NaZWTBXkF1ljroPCM6fOwjS4stqvizpA2SwWTnUOiMRA+cqbQeAcrvl7FUbWlRMDLfKnd76K9XQ=";

    //支付宝公钥
    public static String alipay_public_key ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB";
    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8081/returnUrl";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:8081/returnUrl";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 日志
    public static String log_path = "C:/temp";




    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}