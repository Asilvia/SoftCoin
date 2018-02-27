package com.android.asilvia.softcoin.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


/**
 * Created by asilvia on 30-10-2017.
 */
@Entity(tableName = "LocalCoin")
public class LocalCoin {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo (name = "name")
    private String name;
    @ColumnInfo (name = "key")
    private String key;
    @ColumnInfo (name = "imageUrl")
    private String imageUrl;
    @ColumnInfo (name = "url")
    private String url;
    @ColumnInfo (name = "price")
    private double price;
    @ColumnInfo (name = "userPrice")
    private double userPrice;
    @ColumnInfo (name = "realCoinConverter")
    private String realCoinConverter;
    @ColumnInfo (name = "amount")
    private long amount;


    public LocalCoin(String id, String name, String key, String imageUrl, String url, double price, double userPrice, String realCoinConverter, long amount) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.imageUrl = imageUrl;
        this.url = url;
        this.price = price;
        this.userPrice = userPrice;
        this.realCoinConverter = realCoinConverter;
        this.amount = amount;
    }

  /*  public LocalCoin() {
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getUserPrice() {
        return userPrice;
    }

    public void setUserPrice(double userPrice) {
        this.userPrice = userPrice;
    }

    public String getRealCoinConverter() {
        return realCoinConverter;
    }

    public void setRealCoinConverter(String realCoinConverter) {
        this.realCoinConverter = realCoinConverter;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
