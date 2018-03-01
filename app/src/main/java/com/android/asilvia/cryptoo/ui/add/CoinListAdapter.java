package com.android.asilvia.cryptoo.ui.add;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.asilvia.cryptoo.R;
import com.android.asilvia.cryptoo.vo.CoinsDetails;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by asilvia on 15/02/2018.
 */

public class CoinListAdapter extends ArrayAdapter<CoinsDetails>  implements Filterable {

    Context mContext;
    private CoinsFilter mFilter;
    ArrayList<CoinsDetails> originalCoinList;
    ArrayList<CoinsDetails> filteredList = new ArrayList<>();


    public CoinListAdapter(@NonNull Context context, ArrayList<CoinsDetails> list) {
        super(context, 0 , list);
        mContext = context;
        originalCoinList = list;
        filteredList.addAll(list);
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Nullable
    @Override
    public CoinsDetails getItem(int position) {
        return filteredList.get(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.coin_list_item, parent, false);

        CoinsDetails currentCoin = filteredList.get(position);

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
    public void addList(ArrayList<CoinsDetails> list)
    {
       originalCoinList.addAll(list);
       filteredList.addAll(list);
       notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null)
            mFilter = new CoinsFilter();
        return mFilter;
    }

    public class CoinsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final ArrayList<CoinsDetails>  mList = originalCoinList;
            int count = originalCoinList.size();
            final ArrayList<CoinsDetails> nlist = new ArrayList<CoinsDetails>(count);


            for (int i = 0; i < count; i++) {
                if (mList.get(i).getFullName().toLowerCase().startsWith(filterString)) {
                    CoinsDetails newItem = mList.get(i);
                    nlist.add(newItem);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<CoinsDetails>) results.values;
            notifyDataSetChanged();
        }
    }

}

