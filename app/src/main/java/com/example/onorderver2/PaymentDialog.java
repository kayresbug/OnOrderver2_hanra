package com.example.onorderver2;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

public class PaymentDialog extends Dialog {

    private Context context;
    private PaymentDialogClickListener paymentDialogClickListener;

    private LinearLayout btnCard, btnCash, btnReOrder;

    public PaymentDialog(@NonNull Context context, PaymentDialogClickListener paymentDialogClickListener) {
        super(context);
        this.context = context;
        this.paymentDialogClickListener = paymentDialogClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_payment);

        btnCard = findViewById(R.id.btn_pay_card);
        btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentDialog.this.paymentDialogClickListener.onCardClick();
                dismiss();
            }
        });

        btnCash = findViewById(R.id.btn_pay_cash);
        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentDialog.this.paymentDialogClickListener.onCashClick();
                dismiss();
            }
        });

        btnReOrder = findViewById(R.id.btn_re_order);
        btnReOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentDialog.this.paymentDialogClickListener.onReOrderClick();
                dismiss();
            }
        });
    }
}
