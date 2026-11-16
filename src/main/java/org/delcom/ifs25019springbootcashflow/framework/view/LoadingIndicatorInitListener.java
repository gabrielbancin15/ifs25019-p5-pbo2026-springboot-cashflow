package org.delcom.ifs25019springbootcashflow.framework.view;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

/** Mengatur jeda indikator loading global Vaadin. */
@Component
public class LoadingIndicatorInitListener implements VaadinServiceInitListener {
 @Override public void serviceInit(ServiceInitEvent event){
  event.getSource().addUIInitListener(uiEvent->{
   var config=uiEvent.getUI().getLoadingIndicatorConfiguration();
   config.setFirstDelay(150);
   config.setSecondDelay(600);
   config.setThirdDelay(1500);
  });
 }
}
