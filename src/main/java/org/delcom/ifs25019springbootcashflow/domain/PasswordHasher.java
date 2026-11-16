package org.delcom.ifs25019springbootcashflow.domain;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
public final class PasswordHasher { private PasswordHasher(){} public static String hash(String value){if(value==null)throw new IllegalArgumentException("Password tidak boleh null.");try{return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));}catch(Exception e){throw new IllegalStateException(e);}} public static boolean matches(String raw,String hashed){return raw!=null&&hashed!=null&&hash(raw).equals(hashed);} }
