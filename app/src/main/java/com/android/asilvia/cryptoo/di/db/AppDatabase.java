package com.android.asilvia.cryptoo.di.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.android.asilvia.cryptoo.db.dao.LocalCoinDao;
import com.android.asilvia.cryptoo.db.LocalCoin;

/**
 * Created by asilvia on 30-10-2017.
 */

//Main description of the database

@Database(entities = {LocalCoin.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    abstract public LocalCoinDao localCoinDao();
}


