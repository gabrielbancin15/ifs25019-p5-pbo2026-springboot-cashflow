package org.delcom.ifs25019springbootcashflow.framework.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import org.delcom.ifs25019springbootcashflow.adapter.presenter.FinancePresenter;
import org.delcom.ifs25019springbootcashflow.adapter.storage.FileStorage;
import org.delcom.ifs25019springbootcashflow.domain.SortOption;
import org.delcom.ifs25019springbootcashflow.domain.Transaction;
import org.delcom.ifs25019springbootcashflow.domain.TransactionType;
import org.delcom.ifs25019springbootcashflow.framework.auth.AuthSession;
import org.delcom.ifs25019springbootcashflow.usecase.FinanceUseCase;
import java.util.concurrent.CompletableFuture;
import java.io.InputStream;

@Route(value="", layout=MainLayout.class)
public class FinanceView extends VerticalLayout {
 private final FinanceUseCase finance;
 private final FinancePresenter presenter;
 private final FileStorage storage;
 private final User me;
 private final Grid<Transaction> grid=new Grid<>(Transaction.class,false);
 private final Span balance=new Span(),incomeTotal=new Span(),expenseTotal=new Span();
 private final TextField search=new TextField("Cari keterangan");
 private final ComboBox<SortOption> sort=new ComboBox<>("Urutkan");

 public FinanceView(FinanceUseCase finance,FinancePresenter presenter,FileStorage storage){
  this.finance=finance;this.presenter=presenter;this.storage=storage;this.me=AuthSession.user();
  setWidthFull();
  if(me==null){add(new Span("Sesi tidak ditemukan. Silakan login kembali."));return;}
  add(new H1("Dashboard Cashflow"),new Span("Halo, "+me.fullName()+" ("+me.role()+")"));
  HorizontalLayout cards=new HorizontalLayout(card("Saldo",balance),card("Pemasukan",incomeTotal),card("Pengeluaran",expenseTotal));cards.setWidthFull();add(cards);
  search.setClearButtonVisible(true);search.setWidth("250px");sort.setItems(SortOption.values());sort.setItemLabelGenerator(v->switch(v){case NEWEST->"Terbaru";case OLDEST->"Terlama";case AMOUNT_HIGH->"Jumlah terbesar";case AMOUNT_LOW->"Jumlah terkecil";case INCOME_FIRST->"Pemasukan dulu";case EXPENSE_FIRST->"Pengeluaran dulu";});sort.setValue(SortOption.NEWEST);
  Button reload=new Button("Muat ulang",e->refresh());
  HorizontalLayout tools=new HorizontalLayout(search,sort,reload);
  tools.add(new Button("+ Pemasukan",e->addDialog(TransactionType.INCOME)),new Button("- Pengeluaran",e->addDialog(TransactionType.EXPENSE)));
  add(tools);
  grid.addColumn(Transaction::id).setHeader("ID");
  grid.addColumn(Transaction::description).setHeader("Keterangan");
  grid.addColumn(t->presenter.money(t.amount())).setHeader("Jumlah");
  grid.addColumn(t->t.type()==TransactionType.INCOME?"Pemasukan":"Pengeluaran").setHeader("Tipe");
  grid.addColumn(t->t.createdAt().toString()).setHeader("Waktu");
  grid.addComponentColumn(t->{
   if(t.proofPath()==null)return new Span("—");
   String path=t.proofPath().toLowerCase();
   if(path.endsWith(".pdf"))return new Anchor(t.proofPath(),"Buka PDF");
   return new Button("Lihat bukti",e->new ImagePreviewDialog(t.proofPath()).open());
  }).setHeader("Bukti");
  grid.addComponentColumn(t->new HorizontalLayout(new Button("Edit",e->editDialog(t)),new Button("Hapus",e->confirmDelete(t)))).setHeader("Aksi");
  GridStyles.apply(grid);add(grid);
  search.addValueChangeListener(e->refresh());sort.addValueChangeListener(e->refresh());
  refresh();
 }
 private Component card(String title,Component value){VerticalLayout v=new VerticalLayout(new Span(title),value);v.getStyle().set("background","var(--lumo-contrast-5pct)").set("border-radius","12px").set("padding","16px");return v;}
 private void refresh(){balance.setText(presenter.money(finance.balance(me.id())));incomeTotal.setText(presenter.money(finance.total(me.id(),TransactionType.INCOME)));expenseTotal.setText(presenter.money(finance.total(me.id(),TransactionType.EXPENSE)));grid.setItems(finance.list(me.id(),search.getValue(),sort.getValue()==null?SortOption.NEWEST:sort.getValue()));}
 private void confirmDelete(Transaction transaction){
  Dialog confirm=new Dialog();confirm.setHeaderTitle("Konfirmasi penghapusan");
  confirm.add(new Span("Hapus transaksi '"+transaction.description()+"'? Tindakan ini tidak dapat dibatalkan."));
  ProgressBar progress=new ProgressBar();progress.setIndeterminate(true);progress.setVisible(false);progress.setWidthFull();
  Button cancel=new Button("Batal",e->confirm.close());
  Button remove=new Button("Ya, hapus");
  remove.addClickListener(e->{remove.setEnabled(false);progress.setVisible(true);UI ui=UI.getCurrent();CompletableFuture.runAsync(()->{String error=null;try{finance.delete(me.id(),transaction.id());}catch(Exception ex){error=ex.getMessage();}String result=error;ui.access(()->{progress.setVisible(false);remove.setEnabled(true);if(result==null){confirm.close();refresh();Notification.show("Transaksi dihapus");}else Notification.show(result);});});});
  confirm.add(progress);confirm.getFooter().add(cancel,remove);confirm.open();
 }
 private void addDialog(TransactionType type){transactionDialog(type,null);}
 private void editDialog(Transaction transaction){transactionDialog(transaction.type(),transaction);}
 private void transactionDialog(TransactionType type,Transaction existing){
  TextField desc=new TextField("Keterangan");desc.setWidthFull();
  TextField amount=new TextField("Jumlah (Rupiah)");amount.setPlaceholder("Contoh: 150000");amount.setWidthFull();
  if(existing!=null){desc.setValue(existing.description());amount.setValue(String.valueOf(existing.amount()));}
  MemoryBuffer buffer=new MemoryBuffer();Upload upload=new Upload(buffer);upload.setAcceptedFileTypes("image/png","image/jpeg","image/webp","application/pdf");upload.setMaxFiles(1);upload.setMaxFileSize(5*1024*1024);upload.setDropLabel(new Span("Unggah bukti transaksi (maks. 5 MB)"));
  Dialog dialog=new Dialog();dialog.setHeaderTitle(existing==null?(type==TransactionType.INCOME?"Tambah Pemasukan":"Tambah Pengeluaran"):"Edit Transaksi");
  ProgressBar progress=new ProgressBar();progress.setIndeterminate(true);progress.setVisible(false);progress.setWidthFull();
  Button save=new Button("Simpan");
  save.addClickListener(e->{
   final long value;
   try{value=Long.parseLong(amount.getValue().trim());}catch(Exception ex){Notification.show("Jumlah harus berupa angka bulat.");return;}
   String description=desc.getValue();String filename=buffer.getFileName();InputStream uploaded=filename==null||filename.isBlank()?null:buffer.getInputStream();
   save.setEnabled(false);progress.setVisible(true);UI ui=UI.getCurrent();
   CompletableFuture.runAsync(()->{
    String error=null;
    try{
     String proof=existing==null?null:existing.proofPath();
     if(uploaded!=null)proof=storage.store(uploaded,filename);
     if(existing==null)finance.add(me.id(),description,value,type,proof);else finance.update(me.id(),existing.id(),description,value,type,proof);
    }catch(Exception ex){error=ex.getMessage();}
    String result=error;
    ui.access(()->{progress.setVisible(false);save.setEnabled(true);if(result==null){dialog.close();refresh();Notification.show("Transaksi berhasil disimpan");}else Notification.show(result);});
   });
  });
  dialog.add(new VerticalLayout(desc,amount,upload,progress));dialog.getFooter().add(new Button("Batal",e->dialog.close()),save);dialog.open();
 }
}
