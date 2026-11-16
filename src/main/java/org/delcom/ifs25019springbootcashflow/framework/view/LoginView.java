package org.delcom.ifs25019springbootcashflow.framework.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
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

@Route("login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
 public LoginView(AuthUseCase auth){
  setSizeFull();setAlignItems(Alignment.CENTER);setJustifyContentMode(JustifyContentMode.CENTER);
  TextField user=new TextField("Username");PasswordField pass=new PasswordField("Password");
  ProgressBar progress=new ProgressBar();progress.setIndeterminate(true);progress.setVisible(false);progress.setWidth("280px");
  Button login=new Button("Masuk");
  login.addClickListener(e->{String username=user.getValue(),password=pass.getValue();login.setEnabled(false);progress.setVisible(true);UI ui=UI.getCurrent();
   CompletableFuture.runAsync(()->{org.delcom.ifs25019springbootcashflow.domain.User result=null;String error=null;try{result=auth.login(username,password);}catch(Exception ex){error=ex.getMessage();}var signedIn=result;String message=error;ui.access(()->{progress.setVisible(false);login.setEnabled(true);if(message==null){AuthSession.login(signedIn);ui.navigate("");}else Notification.show(message);});});
  });
  add(new H1("Cashflow"),new Span("Catatan keuangan pribadi"),user,pass,progress,login,new Anchor("register","Buat akun"));
 }
 @Override public void beforeEnter(BeforeEnterEvent event){if(AuthSession.user()!=null)event.forwardTo("");}
}
