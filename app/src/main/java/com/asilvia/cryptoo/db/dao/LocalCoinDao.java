package com.asilvia.cryptoo.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.asilvia.cryptoo.db.LocalCoin;

import java.util.List;

/**
 * Created by asilvia on 30-10-2017.
 */

@Dao
public interface LocalCoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LocalCoin localCoin);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(List<LocalCoin> list);

    @Query("SELECT * FROM LocalCoin WHERE name = :name")
    LiveData<LocalCoin> findCoinByName(String name);

    @Query("SELECT * FROM LocalCoin WHERE id = :id")
    LocalCoin findCoinById(String id);

    @Query("SELECT * FROM LocalCoin")
    List<LocalCoin> findAllCoins();

    @Delete
    public void deleteCoin (LocalCoin localCoin);

    @Delete
    public void deleteAllCoins(LocalCoin... localCoins);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCoinList(List<LocalCoin> list);
}
