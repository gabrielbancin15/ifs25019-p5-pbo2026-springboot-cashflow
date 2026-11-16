package org.delcom.ifs25019springbootcashflow.adapter;
import org.delcom.ifs25019springbootcashflow.adapter.storage.FileStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import java.io.ByteArrayInputStream;
import static org.junit.jupiter.api.Assertions.*;
class FileStorageTest {
 @TempDir Path temp;
 @Test void storesAllowedFileExtensionAndContent()throws Exception{
  FileStorage storage=new FileStorage(temp.toString());String path=storage.store(new ByteArrayInputStream("bukti".getBytes()),"bukti.png");
  assertTrue(path.startsWith("/uploads/"));assertTrue(path.endsWith(".png"));
  String name=path.substring(path.lastIndexOf('/')+1);assertEquals("bukti",Files.readString(temp.resolve(name)));
 }
 @Test void replacesUnknownExtension()throws Exception{
  FileStorage storage=new FileStorage(temp.toString());String path=storage.store(new ByteArrayInputStream("data".getBytes()),"script.exe");
  assertTrue(path.endsWith(".bin"));
 }
}
