package org.delcom.ifs25019springbootcashflow.framework.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import org.delcom.ifs25019springbootcashflow.adapter.storage.FileStorage;
import org.delcom.ifs25019springbootcashflow.framework.auth.AuthSession;
import org.delcom.ifs25019springbootcashflow.usecase.UserUseCase;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

@Route(value="profile",layout=MainLayout.class)
public class ProfileView extends VerticalLayout {
 public ProfileView(UserUseCase users,FileStorage storage){
  var me=AuthSession.user();
  if(me==null){add(new H1("Sesi berakhir"),new Anchor("login","Masuk kembali"));return;}
  TextField name=new TextField("Nama lengkap",me.fullName());
  PasswordField pass=new PasswordField("Password baru (opsional)");
  MemoryBuffer buffer=new MemoryBuffer();Upload photo=new Upload(buffer);photo.setAcceptedFileTypes("image/png","image/jpeg","image/webp");photo.setMaxFiles(1);photo.setMaxFileSize(3*1024*1024);
  Image currentPhoto=new Image(me.photoPath()==null?"":"/uploads/"+me.photoPath().substring(me.photoPath().lastIndexOf('/')+1),"Foto profil");currentPhoto.setWidth("120px");currentPhoto.setHeight("120px");currentPhoto.getStyle().set("object-fit","cover").set("border-radius","50%");
  ProgressBar progress=new ProgressBar();progress.setIndeterminate(true);progress.setVisible(false);progress.setWidthFull();
  Button save=new Button("Simpan perubahan");
  save.addClickListener(e->{
   String fullName=name.getValue();String newPassword=pass.getValue();String filename=buffer.getFileName();InputStream uploaded=filename==null||filename.isBlank()?null:buffer.getInputStream();
   save.setEnabled(false);progress.setVisible(true);UI ui=UI.getCurrent();
   CompletableFuture.runAsync(()->{
    org.delcom.ifs25019springbootcashflow.domain.User updated=null;String error=null;
    try{
     updated=users.updateName(me,fullName);
     if(newPassword!=null&&!newPassword.isBlank())updated=users.updatePassword(updated,newPassword);
     if(uploaded!=null)updated=users.updatePhoto(updated,storage.store(uploaded,filename));
    }catch(Exception ex){error=ex.getMessage();}
    var resultUser=updated;String resultError=error;
    ui.access(()->{progress.setVisible(false);save.setEnabled(true);if(resultError==null){AuthSession.login(resultUser);Notification.show("Profil berhasil diperbarui");}else Notification.show(resultError);});
   });
  });
  add(new H1("Profil Pengguna"),currentPhoto,new com.vaadin.flow.component.html.Span("Username: "+me.username()),name,pass,new com.vaadin.flow.component.html.Span("Foto profil (maks. 3 MB)"),photo,progress,save,new Anchor("","Kembali ke dashboard"));
 }
}
