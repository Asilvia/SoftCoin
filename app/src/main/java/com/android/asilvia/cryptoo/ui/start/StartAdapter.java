package com.android.asilvia.cryptoo.ui.start;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.asilvia.cryptoo.R;
import com.android.asilvia.cryptoo.db.LocalCoin;
import com.android.asilvia.cryptoo.ui.base.navigation.AppNavigation;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by ana.medeira on 25-7-2017.
 */

public class StartAdapter extends RecyclerView.Adapter<StartAdapter.ViewHolder>
{

    private List<LocalCoin> data;
    private Context context;
    private String symbol;
    private boolean market;




    public static class ViewHolder extends RecyclerView.ViewHolder  {
    TextView name;
    TextView value;
    ImageView icon;
    TextView value_percentage;
    ImageView indicator;
    RelativeLayout card_indicator;

    public ViewHolder(View view) {
        super(view);
        name =(TextView) view.findViewById(R.id.name);
        value = (TextView)view.findViewById(R.id.value);
        icon = (ImageView) view.findViewById(R.id.icon);
        value_percentage = (TextView)view.findViewById(R.id.value_percentage);
        indicator=(ImageView)view.findViewById(R.id.indicator);
        card_indicator = (RelativeLayout)view.findViewById(R.id.card_indicator);
    }


    }

    public StartAdapter(Context context, List<LocalCoin> data, String symbol, boolean market) {
        this.data = data;
        this.context = context;
        this.symbol = symbol;
        this.market = market;
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
        String tvText = item.getName() + " (" +item.getKey() + ")";
        holder.name.setText(tvText);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppNavigation.goToDetailsActivity(context, item.getId(), tvText);
            }
        });
        Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.icon)
        ;

        if(market) {

            holder.value_percentage.setText(String.format("%.2f", item.getIndex()) + "%");
            holder.value.setText(symbol + String.format("%.2f", item.getPrice()));
            setIndicator(holder, item.getIndex());
        }
        else
        {
            double percentage = getUserPercentage(item);
            holder.value_percentage.setText(String.format("%.2f", percentage) + "%");
            holder.value.setText(symbol + item.getUserProfit());
            setIndicator(holder, percentage);
        }


        //  setBackgroundColor(holder.cardView, item);

    }

    private void setIndicator(ViewHolder holder, double percentage) {
        if(percentage > 0)
        {
           holder.card_indicator.setBackgroundResource(R.color.green);
           holder.indicator.setBackgroundResource(R.drawable.ic_arrow_drop_up);
        }
        else
        {
            holder.card_indicator.setBackgroundResource(R.color.pink);
            holder.indicator.setBackgroundResource(R.drawable.ic_arrow_drop_down);

        }
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


    public void updateSymbolAndMarket(String symbol, boolean isMarket)
    {
        this.symbol = symbol;
        this.market = isMarket;
    }





    public void setCoin(List<LocalCoin> coin) {
        this.data = new ArrayList<>();
        this.data.addAll(coin);
        notifyDataSetChanged();
    }


    public double getUserPercentage (LocalCoin localCoin){
        double finalPrice = localCoin.getPrice();
        double userPrice = localCoin.getUserPrice();
        return (finalPrice - userPrice)/finalPrice *100;
    }

}
