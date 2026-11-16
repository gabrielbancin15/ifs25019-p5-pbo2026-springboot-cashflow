package org.delcom.ifs25019springbootcashflow.framework.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/** Kartu UI sederhana yang dapat dipakai ulang antar halaman. */
public class PageCard extends VerticalLayout {
 public PageCard(String title,Component content){
  add(new H3(title),content);
  setPadding(true);setSpacing(true);setWidthFull();
  getStyle().set("background","var(--lumo-base-color)")
   .set("border","1px solid var(--lumo-contrast-10pct)")
   .set("border-radius","14px");
 }
}
