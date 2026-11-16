package org.delcom.ifs25019springbootcashflow.framework.view;

import com.vaadin.flow.component.grid.Grid;

/** Pengaturan tampilan grid agar konsisten. */
public final class GridStyles {
 private GridStyles(){}
 public static <T> Grid<T> apply(Grid<T> grid){
  grid.setWidthFull();grid.setAllRowsVisible(true);
  grid.getStyle().set("border-radius","12px").set("overflow","hidden");
  return grid;
 }
}
