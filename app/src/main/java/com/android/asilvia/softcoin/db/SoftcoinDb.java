package com.android.asilvia.softcoin.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.android.asilvia.softcoin.vo.LocalCoin;

/**
 * Created by asilvia on 30-10-2017.
 */

//Main description of the database

@Database(entities = {LocalCoin.class}, version = 1)
public abstract class SoftcoinDb extends RoomDatabase {
    abstract public LocalCoinDao localCoinDao();
}


