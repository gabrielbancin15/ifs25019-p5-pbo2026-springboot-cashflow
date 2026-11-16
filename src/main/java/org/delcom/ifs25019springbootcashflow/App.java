package org.delcom.ifs25019springbootcashflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.delcom.ifs25019springbootcashflow.config.DotEnv;
import org.delcom.ifs25019springbootcashflow.usecase.AuthUseCase;

@SpringBootApplication
@Theme("cashflow")
@Push
public class App {
  public static void main(String[] args) { DotEnv.load(); SpringApplication.run(App.class, args); }
  @Bean CommandLineRunner seedAdmin(AuthUseCase auth, @Value("${app.root-username}") String username, @Value("${app.root-password}") String password) {
    return args -> auth.ensureRootAdmin(username,password);
  }
}
