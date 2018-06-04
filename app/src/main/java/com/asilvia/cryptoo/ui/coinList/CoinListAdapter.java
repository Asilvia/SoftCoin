package com.asilvia.cryptoo.ui.coinList;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asilvia.cryptoo.R;
import com.asilvia.cryptoo.ui.add.AddCoinActivity;
import com.asilvia.cryptoo.vo.CoinsDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by asilvia on 15/02/2018.
 */

public class CoinListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements Filterable {

    Context mContext;
    private CoinsFilter mFilter;
    ArrayList<CoinsDetails> originalCoinList;
    ArrayList<CoinsDetails> filteredList = new ArrayList<>();
    private NativeAd ad;
    private static final int AD_INDEX = 1;

    //New version
    private int adDisplayFrequency = 10;
    private static int POST_TYPE = 0;
    private static int AD_TYPE = 1;
    private NativeAdsManager mNativeAdsManager;
    private List<NativeAd> mAdItems;



    public CoinListAdapter(@NonNull Context context, ArrayList<CoinsDetails> list, NativeAdsManager adsManager) {

        mContext = context;
        originalCoinList = list;
        filteredList.addAll(list);
        mNativeAdsManager = adsManager;
        mAdItems = new ArrayList<>();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AD_TYPE) {
            View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.facebook_native_ad, parent, false);
            return new AdHolder(inflatedView);
        } else {
            View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coin_list_item, parent, false);
            return new PostHolder(inflatedView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == AD_TYPE) {


            if (mAdItems.size() > position / adDisplayFrequency) {
                ad = mAdItems.get(position / adDisplayFrequency);
            } else {
                ad = mNativeAdsManager.nextNativeAd();
                mAdItems.add(ad);
            }

            if (ad != null) {
                setAdsLayout((AdHolder) holder, ad);
            }
        } else {
            PostHolder postHolder = (PostHolder) holder;
            CoinsDetails currentCoin;


            currentCoin = getItem(position);

            if(currentCoin == filteredList.get(0))
                postHolder.cardview.setBackgroundResource(R.drawable.card_round_top_corners);
            else if(currentCoin == filteredList.get(filteredList.size()-1))
                postHolder.cardview.setBackgroundResource(R.drawable.card_round_bottom_corners);
            else
                postHolder.cardview.setBackgroundColor(mContext.getColor(R.color.cardBackground));

            postHolder.name.setText(currentCoin.getCoinName());
            postHolder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Timber.d(" go to next activity" + currentCoin.getCoinName()  );
                    ((AddCoinActivity)mContext).openInformation(currentCoin);
                }
            });
            postHolder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((AddCoinActivity)mContext).openDialog(currentCoin);
                }
            });

            Timber.d("img: " + currentCoin.getImageUrl());
            Glide.with(mContext).load(currentCoin.getImageUrl()).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher)).into(postHolder.image);
        }
    }

    private void setAdsLayout(AdHolder holder, NativeAd ad) {
        AdHolder adHolder = holder;
        adHolder.tvAdTitle.setText(ad.getAdTitle());
        adHolder.tvAdBody.setText(ad.getAdBody());
        adHolder.tvAdSocialContext.setText(ad.getAdSocialContext());
        adHolder.btnAdCallToAction.setText(ad.getAdCallToAction());
        NativeAd.Image adIcon = ad.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, adHolder.ivAdIcon);
        ad.registerViewForInteraction(adHolder.itemView);
    }

    @Override
    public int getItemViewType(int position) {
        if(mNativeAdsManager.isLoaded())
        return position == 1 ? AD_TYPE : POST_TYPE;
        else
            return POST_TYPE;
    }

    @Override
    public int getItemCount() {
        int total = 0;
        if(filteredList.size() != 0) {
            if (ad != null) {
                total = filteredList.size() + 1;
            }
            else {
                total = filteredList.size();
            }
        }
        return total;
    }

    public CoinsDetails getItem(int position) {
        if(position == 0) {
            return filteredList.get(position);
        }
        else {
            if (ad != null)
                return filteredList.get(position - 1);
            else
                return filteredList.get(position);
        }
    }

    private static class PostHolder extends RecyclerView.ViewHolder {
        RelativeLayout cardview;
        ImageView image;
        TextView name;
        ImageView plus;

        PostHolder(View view) {
            super(view);
            cardview = (RelativeLayout) view.findViewById(R.id.card_view);
            image = (ImageView) view.findViewById(R.id.icon);
            name = (TextView) view.findViewById(R.id.name);
            plus = (ImageView) view.findViewById(R.id.plus);
        }
    }

    private static class AdHolder extends RecyclerView.ViewHolder {
        ImageView ivAdIcon;
        TextView tvAdTitle;
        TextView tvAdBody;
        TextView tvAdSocialContext;
        Button btnAdCallToAction;


        AdHolder(View view) {
            super(view);

            tvAdTitle = (TextView) view.findViewById(R.id.native_ad_title);
            tvAdBody = (TextView) view.findViewById(R.id.native_ad_body);
            tvAdSocialContext = (TextView) view.findViewById(R.id.native_ad_social_context);
            btnAdCallToAction = (Button) view.findViewById(R.id.native_ad_call_to_action);
            ivAdIcon = (ImageView) view.findViewById(R.id.native_ad_icon);
        }
    }



    public void addList(ArrayList<CoinsDetails> list)
    {
        int i=0;
       originalCoinList.addAll(list);
       if(originalCoinList.size() >= 10) {
           while (filteredList.size() < 4) {
                filteredList.add(originalCoinList.get(i));
                i++;
           }
       }
       notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null)
            mFilter = new CoinsFilter();
        return mFilter;
    }

    public void setNativeAd(NativeAd nativeAd) {
        this.ad = nativeAd;
        notifyDataSetChanged();
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

