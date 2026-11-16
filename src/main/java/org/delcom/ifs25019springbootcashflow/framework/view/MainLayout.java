package org.delcom.ifs25019springbootcashflow.framework.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;
import org.delcom.ifs25019springbootcashflow.domain.Role;
import org.delcom.ifs25019springbootcashflow.framework.auth.AuthSession;

public class MainLayout extends AppLayout implements BeforeEnterObserver {
 public MainLayout(){
  H3 brand=new H3("Cashflow");
  HorizontalLayout nav=new HorizontalLayout(new RouterLink("Dashboard",FinanceView.class),new RouterLink("Profil",ProfileView.class));
  if(AuthSession.user()!=null && AuthSession.user().role()==Role.ADMIN){nav.add(new RouterLink("Pengguna",UsersView.class));}
  Button logout=new Button("Keluar",e->{AuthSession.logout();getUI().ifPresent(ui->ui.navigate("login"));});
  HorizontalLayout bar=new HorizontalLayout(brand,nav,logout);
  bar.setWidthFull();bar.setAlignItems(Alignment.CENTER);bar.expand(nav);
  addToNavbar(bar);
 }
 @Override public void beforeEnter(BeforeEnterEvent event){
  String path=event.getLocation().getPath();
  var user=AuthSession.user();
  if(user==null){event.forwardTo("login");return;}
  if((path.equals("users")||path.equals("user-transactions"))&&user.role()!=Role.ADMIN)event.forwardTo("");
 }
}
