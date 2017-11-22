package com.android.asilvia.softcoin.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by asilvia on 30-10-2017.
 */
@Entity
public class LocalCoin {

    @PrimaryKey
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
}
