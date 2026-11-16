package org.delcom.ifs25019springbootcashflow.config;

import org.delcom.ifs25019springbootcashflow.adapter.storage.FileStorage;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
 private final FileStorage storage;
 public WebConfig(FileStorage storage){this.storage=storage;}
 @Override public void addResourceHandlers(ResourceHandlerRegistry registry){
  String location=storage.root().toUri().toString();
  if(!location.endsWith("/"))location+="/";
  registry.addResourceHandler("/uploads/**").addResourceLocations(location);
 }
}
