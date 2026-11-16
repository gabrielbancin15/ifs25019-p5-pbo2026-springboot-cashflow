package org.delcom.ifs25019springbootcashflow.framework.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import org.delcom.ifs25019springbootcashflow.framework.auth.AuthSession;
import org.delcom.ifs25019springbootcashflow.usecase.AuthUseCase;
import java.util.concurrent.CompletableFuture;

@Route("register")
public class RegisterView extends VerticalLayout implements BeforeEnterObserver {
 public RegisterView(AuthUseCase auth){
  setSizeFull();setAlignItems(Alignment.CENTER);setJustifyContentMode(JustifyContentMode.CENTER);
  TextField name=new TextField("Nama lengkap"),user=new TextField("Username");PasswordField pass=new PasswordField("Password (min. 6 karakter)");
  ProgressBar progress=new ProgressBar();progress.setIndeterminate(true);progress.setVisible(false);progress.setWidth("280px");
  Button register=new Button("Daftar");
  register.addClickListener(e->{String fullName=name.getValue(),username=user.getValue(),password=pass.getValue();register.setEnabled(false);progress.setVisible(true);UI ui=UI.getCurrent();
   CompletableFuture.runAsync(()->{org.delcom.ifs25019springbootcashflow.domain.User result=null;String error=null;try{result=auth.register(username,password,fullName);}catch(Exception ex){error=ex.getMessage();}var signedIn=result;String message=error;ui.access(()->{progress.setVisible(false);register.setEnabled(true);if(message==null){AuthSession.login(signedIn);ui.navigate("");}else Notification.show(message);});});
  });
  add(new H1("Daftar Cashflow"),name,user,pass,progress,register,new Anchor("login","Sudah punya akun? Masuk"));
 }
 @Override public void beforeEnter(BeforeEnterEvent event){if(AuthSession.user()!=null)event.forwardTo("");}
}
