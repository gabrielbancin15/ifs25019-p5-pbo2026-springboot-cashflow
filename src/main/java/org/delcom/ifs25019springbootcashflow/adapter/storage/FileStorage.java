package org.delcom.ifs25019springbootcashflow.adapter.storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.io.InputStream;
import java.util.UUID;

@Component
public class FileStorage {
 private final Path root;
 public FileStorage(@Value("${app.upload-dir}") String dir){root=Paths.get(dir).toAbsolutePath().normalize();}
 public String store(MultipartFile file){
  if(file==null||file.isEmpty()) return null;
  try{return store(file.getInputStream(),file.getOriginalFilename());}
  catch(Exception e){throw new IllegalStateException("Gagal membaca file",e);}
 }
 public String store(InputStream input,String originalName){
  try{
   Files.createDirectories(root);
   String ext="";
   if(originalName!=null&&originalName.contains(".")) ext=originalName.substring(originalName.lastIndexOf('.')).toLowerCase();
   if(!ext.matches("\\.(png|jpg|jpeg|webp|pdf)")) ext=".bin";
   String name=UUID.randomUUID()+ext;
   try(InputStream source=input){Files.copy(source,root.resolve(name),StandardCopyOption.REPLACE_EXISTING);}
   return "/uploads/"+name;
  }catch(Exception e){throw new IllegalStateException("Gagal menyimpan file",e);}
 }
 public Path root(){return root;}
}
