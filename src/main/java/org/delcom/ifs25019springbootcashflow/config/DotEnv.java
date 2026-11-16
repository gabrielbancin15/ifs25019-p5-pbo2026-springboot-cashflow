package org.delcom.ifs25019springbootcashflow.config;

import java.nio.file.Files;
import java.nio.file.Path;

/** Memuat pasangan KEY=VALUE dari .env ke system properties sebelum Spring mulai. */
public final class DotEnv {
 private DotEnv(){}
 public static void load(){
  Path file=Path.of(".env");
  if(!Files.exists(file))return;
  try{
   for(String raw:Files.readAllLines(file)){
    String line=raw.trim();
    if(line.isEmpty()||line.startsWith("#")||!line.contains("="))continue;
    int split=line.indexOf('=');String key=line.substring(0,split).trim();String value=line.substring(split+1).trim();
    if((value.startsWith("\"")&&value.endsWith("\""))||(value.startsWith("'")&&value.endsWith("'")))value=value.substring(1,value.length()-1);
    if(!key.isBlank()&&System.getenv(key)==null&&System.getProperty(key)==null)System.setProperty(key,value);
   }
  }catch(Exception e){throw new IllegalStateException("Gagal membaca file .env",e);}
 }
}
