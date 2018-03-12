package com.android.asilvia.cryptoo.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by asilvia on 11/03/2018.
 */

public class CoinsPrice {


        @SerializedName("RAW")
        @Expose
        private Map<String, Map<String, CoinsPriceRaw>> rAW;
        @SerializedName("DISPLAY")
        @Expose
        private Map<String, Map<String, CoinsPriceDisplay>> dISPLAY;

        public Map<String, Map<String, CoinsPriceRaw>> getRAW() {
            return rAW;
        }

        public void setRAW(Map<String, Map<String, CoinsPriceRaw>> rAW) {
            this.rAW = rAW;
        }

        public Map<String, Map<String, CoinsPriceDisplay>> getDISPLAY() {
            return dISPLAY;
        }

        public void setDISPLAY(Map<String, Map<String, CoinsPriceDisplay>> dISPLAY) {
            this.dISPLAY = dISPLAY;
        }


}
