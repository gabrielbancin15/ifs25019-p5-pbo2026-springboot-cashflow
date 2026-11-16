package org.delcom.ifs25019springbootcashflow.framework.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.notification.Notification;
import org.delcom.ifs25019springbootcashflow.domain.Role;
import org.delcom.ifs25019springbootcashflow.domain.Transaction;
import org.delcom.ifs25019springbootcashflow.adapter.presenter.FinancePresenter;
import org.delcom.ifs25019springbootcashflow.framework.auth.AuthSession;
import org.delcom.ifs25019springbootcashflow.usecase.FinanceUseCase;
import org.delcom.ifs25019springbootcashflow.usecase.UserUseCase;
import com.vaadin.flow.component.combobox.ComboBox;

@Route(value="user-transactions", layout=MainLayout.class)
public class UserTransactionsView extends VerticalLayout {
 public UserTransactionsView(FinanceUseCase finance,UserUseCase users,FinancePresenter presenter){
  var me=AuthSession.user();
  if(me==null||me.role()!=Role.ADMIN){Notification.show("Halaman khusus ADMIN");getUI().ifPresent(ui->ui.navigate(""));return;}
  ComboBox<org.delcom.ifs25019springbootcashflow.domain.User> select=new ComboBox<>("Pilih pengguna");
  select.setItems(users.list());select.setItemLabelGenerator(u->u.username()+" — "+u.fullName());
  Grid<Transaction> grid=new Grid<>(Transaction.class,false);
  grid.addColumn(Transaction::id).setHeader("ID");
  grid.addColumn(Transaction::description).setHeader("Keterangan");
  grid.addColumn(t->presenter.money(t.amount())).setHeader("Jumlah");
  grid.addColumn(t->t.type().name()).setHeader("Tipe");
  grid.addColumn(t->t.createdAt().toString()).setHeader("Waktu");
  grid.addComponentColumn(t->{if(t.proofPath()==null)return new com.vaadin.flow.component.html.Span("—");if(t.proofPath().toLowerCase().endsWith(".pdf"))return new Anchor(t.proofPath(),"Buka PDF");return new com.vaadin.flow.component.button.Button("Lihat bukti",e->new ImagePreviewDialog(t.proofPath()).open());}).setHeader("Bukti");
  GridStyles.apply(grid);
  select.addValueChangeListener(e->{if(e.getValue()!=null)grid.setItems(finance.listForAdmin(e.getValue().id()));else grid.setItems(java.util.List.of());});
  add(new H1("Peninjauan Transaksi Pengguna"),select,grid,new Anchor("users","Kembali ke pengguna"));
 }
}
