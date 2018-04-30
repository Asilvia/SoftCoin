package com.android.asilvia.cryptoo.ui.add;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.asilvia.cryptoo.R;
import com.android.asilvia.cryptoo.vo.CoinsDetails;
import com.bumptech.glide.Glide;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by asilvia on 15/02/2018.
 */

public class CoinListAdapter extends ArrayAdapter<CoinsDetails>  implements Filterable {

    Context mContext;
    private CoinsFilter mFilter;
    ArrayList<CoinsDetails> originalCoinList;
    ArrayList<CoinsDetails> filteredList = new ArrayList<>();
    ArrayList<String> lastSearched = new ArrayList<>();
    private NativeAd ad;
    private static final int AD_INDEX = 1;


    public CoinListAdapter(@NonNull Context context, ArrayList<CoinsDetails> list, ArrayList<String> lastsearch) {
        super(context, 0 , list);
        mContext = context;
        originalCoinList = list;
        filteredList.addAll(list);
        lastSearched.addAll(lastsearch);

    }

    @Override
    public int getCount() {
        if(filteredList.size() == 0) {
            return filteredList.size();
        }
        else {
            if (ad != null)
                return filteredList.size() + 1;
            else
                return filteredList.size();
        }

    }

    @Nullable
    @Override
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


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if (position == AD_INDEX && ad != null){
            listItem = setAdsView(parent);
        }else {


                listItem = LayoutInflater.from(mContext).inflate(R.layout.coin_list_item, parent, false);

            if(position != 0 && ad !=null)
            {
                position = position - 1;
            }
            CoinsDetails currentCoin = filteredList.get(position);

            ImageView image = (ImageView) listItem.findViewById(R.id.icon);
            Timber.d("img: " + currentCoin.getImageUrl());
                Glide.with(mContext)
                    .load(currentCoin.getImageUrl())
                    .asBitmap()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(image);


            TextView name = (TextView) listItem.findViewById(R.id.name);
            name.setText(currentCoin.getCoinName());

        }
        return listItem;
    }

    @NonNull
    private View setAdsView(@NonNull ViewGroup parent) {
        View listItem;
        listItem = LayoutInflater.from(mContext).inflate(R.layout.facebook_native_ad, parent, false);
        ImageView nativeAdIcon = (ImageView) listItem.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView) listItem.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = (TextView) listItem.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = (TextView) listItem.findViewById(R.id.native_ad_body);
        Button nativeAdCallToAction = (Button) listItem.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(ad.getAdTitle());
        nativeAdSocialContext.setText(ad.getAdSocialContext());
        nativeAdBody.setText(ad.getAdBody());
        nativeAdCallToAction.setText(ad.getAdCallToAction());

        // Download and display the ad icon.
        NativeAd.Image adIcon = ad.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Download and display the cover image.
        // nativeAdMedia.setNativeAd(ad);

        // Add the AdChoices icon
        LinearLayout adChoicesContainer = (LinearLayout) listItem.findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(getContext(), ad, true);
        adChoicesContainer.addView(adChoicesView);

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        ad.registerViewForInteraction(listItem,clickableViews);
        return listItem;
    }

    public void addList(ArrayList<CoinsDetails> list)
    {
        int i=0;
       originalCoinList.addAll(list);
       if(originalCoinList.size() >= 10) {
           while (filteredList.size() < 5) {
                filteredList.add(originalCoinList.get(i));
                i++;
           }
       }
      /* if(lastSearched.size() > 0)
       {
           for (int i=0; i<lastSearched.size(); i++)
           {
               for(int j=0; j<originalCoinList.size(); j++)
               {
                   if(lastSearched.get(i).equals(originalCoinList.get(j).getId()))
                   {
                       filteredList.add(originalCoinList.get(j));
                       break;
                   }
               }
           }
       }
       else
       {
           filteredList.addAll(list);
       }
*/
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

