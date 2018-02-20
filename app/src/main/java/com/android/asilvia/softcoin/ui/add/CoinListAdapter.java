package com.android.asilvia.softcoin.ui.add;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.asilvia.softcoin.R;
import com.android.asilvia.softcoin.vo.CoinsDetails;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by asilvia on 15/02/2018.
 */

public class CoinListAdapter extends ArrayAdapter<CoinsDetails> {

    Context mContext;
    ArrayList<CoinsDetails> coinList;


    public CoinListAdapter(@NonNull Context context, ArrayList<CoinsDetails> list) {
        super(context, 0 , list);
        mContext = context;
        coinList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.coin_list_item, parent, false);

        CoinsDetails currentCoin = coinList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.icon);
        Timber.d("img: " + currentCoin.getImageUrl());
        Glide.with(mContext)
                .load(currentCoin.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .into(image)
        ;


        TextView name = (TextView) listItem.findViewById(R.id.Itemname);
        name.setText(currentCoin.getCoinName());


        return listItem;
    }
}

