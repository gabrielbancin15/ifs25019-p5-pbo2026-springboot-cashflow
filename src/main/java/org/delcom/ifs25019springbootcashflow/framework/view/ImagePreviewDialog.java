package org.delcom.ifs25019springbootcashflow.framework.view;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;

/** Dialog untuk memperbesar bukti transaksi atau foto profil. */
public class ImagePreviewDialog extends Dialog {
 public ImagePreviewDialog(String imageUrl){
  setHeaderTitle("Pratinjau bukti transaksi");
  Image image=new Image(imageUrl,"Pratinjau gambar");
  image.setMaxWidth("min(80vw, 900px)");
  image.setMaxHeight("75vh");
  image.getStyle().set("object-fit","contain");
  add(image);
  setCloseOnEsc(true);setCloseOnOutsideClick(true);
  getFooter().add(new com.vaadin.flow.component.button.Button("Tutup",e->close()));
 }
}
