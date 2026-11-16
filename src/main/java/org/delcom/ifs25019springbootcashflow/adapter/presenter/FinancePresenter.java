package org.delcom.ifs25019springbootcashflow.adapter.presenter;
import java.text.NumberFormat;
import java.util.Locale;
import org.springframework.stereotype.Component;
@Component
public class FinancePresenter {
 private final NumberFormat fmt=NumberFormat.getNumberInstance(Locale.US);
 public String money(long value){return "Rp "+fmt.format(value)+".00";}
}
