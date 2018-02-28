package com.android.asilvia.cryptoo.ui.start;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.android.asilvia.cryptoo.R;
import com.android.asilvia.cryptoo.db.LocalCoin;
import com.android.asilvia.cryptoo.ui.base.navigation.AppNavigation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ana.medeira on 25-7-2017.
 */

public class StartAdapter extends RecyclerView.Adapter<StartAdapter.ViewHolder>
{

    private List<LocalCoin> data;
    private Context context;




    public static class ViewHolder extends RecyclerView.ViewHolder  {
    TextView name;
    TextView value;
        CardView cardView;
    public ViewHolder(View view) {
        super(view);
        name =(TextView) view.findViewById(R.id.name);
        value = (TextView)view.findViewById(R.id.value);
        cardView = (CardView)view.findViewById(R.id.card_view);
    }


    }

    public StartAdapter(Context context, List<LocalCoin> data) {
        this.data = data;
        this.context = context;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public StartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.coin_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final LocalCoin item = (LocalCoin) getItem(position);
        holder.name.setText(item.getKey());
        holder.value.setText(String.format("%.2f",item.getPrice()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppNavigation.goToDetailsActivity(context, item.getId());
            }
        });
        setBackgroundColor(holder.cardView, item);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public Object getItem(int position) {
        return data.get(position);
    }
    public void removeFromList(int position)
    {
        data.remove(position);
    }






    public void setCoin(List<LocalCoin> coin) {
        this.data = new ArrayList<>();
        this.data.addAll(coin);
        notifyDataSetChanged();
    }

    public void setBackgroundColor(CardView card, LocalCoin coin)
    {
        Double actualPrice = coin.getPrice();
        Double userPrice = coin.getUserPrice();
        Long amount = coin.getAmount();
        if(userPrice != 0 && amount != 0)
        {
            double result = amount * (actualPrice - userPrice);
            if(result > 50)
            {
                card.setBackgroundResource(R.drawable.card_view_green);
            }
            else if(result < -50)
            {
                card.setBackgroundResource(R.drawable.card_view_red);
            }
            else
            {
                card.setBackgroundResource(R.drawable.card_view_blue);
            }
        }
        else
        {
            card.setBackgroundResource(R.drawable.card_view_null);
        }


    }
}
