package org.delcom.ifs25019springbootcashflow.framework.auth;
import com.vaadin.flow.server.VaadinSession; import org.delcom.ifs25019springbootcashflow.domain.User;
public final class AuthSession {private static final String KEY="cashflow.user"; private AuthSession(){} public static void login(User u){VaadinSession.getCurrent().setAttribute(KEY,u);} public static User user(){return VaadinSession.getCurrent()==null?null:(User)VaadinSession.getCurrent().getAttribute(KEY);} public static void logout(){if(VaadinSession.getCurrent()!=null)VaadinSession.getCurrent().setAttribute(KEY,null);} }
