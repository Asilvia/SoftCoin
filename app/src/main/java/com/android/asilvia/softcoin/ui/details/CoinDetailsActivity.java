package com.android.asilvia.softcoin.ui.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;



public class CoinDetailsActivity extends AppCompatActivity {
    public static final String TAG = "CoinDetailsActivity";
 /*   String id;
    RealmController mControler;
    Realm mRealm;
    Double mPrice;
    long mAmount;
    LocalCoin coin_card;
    TextView percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_details);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Log.d(TAG, id);
        mControler = RealmController.with(this);
        mRealm = mControler.getRealm();
        coin_card = mControler.getCoinWithId(id);
        getSupportActionBar().setTitle(coin_card.getName());

        TextView price = (TextView) findViewById(R.id.tvPriceNow);
        TextView myPrice = (TextView) findViewById(R.id.tvPrice);
        final EditText amount = (EditText) findViewById(R.id.etAmount);

        percentage = (TextView) findViewById(R.id.tvPercentage);

        mPrice = coin_card.getUserPrice();
        price.setText(String.valueOf(coin_card.getPrice() + " " + coin_card.getRealCoinConverter()));
        if(mPrice != 0) {
            myPrice.setText(String.valueOf(mPrice));
        }

        percentage.setText(String.valueOf(coin_card.getPrice() - mPrice));
        mAmount = coin_card.getAmount();
        if(mAmount != 0) {
            amount.setText(String.valueOf(coin_card.getAmount()));
        }


        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0 && s.length() < 20) {
                    mAmount = Long.valueOf(s.toString());
                    percentage.setText(String.valueOf((coin_card.getPrice() - mPrice)* mAmount));
                }
                else
                {
                    Snackbar.make(amount, "Invalid amount", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        myPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() != 0) {
                    mPrice = Double.valueOf(s.toString());
                    percentage.setText(String.valueOf(coin_card.getPrice() - mPrice));
                }

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                coin_card.setUserPrice(mPrice);
                coin_card.setAmount(mAmount);
                realm.copyToRealmOrUpdate(coin_card);
                realm.commitTransaction();

            }
        });



    }*/
}
