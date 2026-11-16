package org.delcom.ifs25019springbootcashflow.adapter;
import org.delcom.ifs25019springbootcashflow.adapter.presenter.FinancePresenter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class FinancePresenterTest {@Test void formatsRupiah(){String value=new FinancePresenter().money(1250000);assertTrue(value.startsWith("Rp "));assertTrue(value.contains("1"));}}
