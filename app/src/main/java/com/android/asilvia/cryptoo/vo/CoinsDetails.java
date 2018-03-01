package com.android.asilvia.cryptoo.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by asilvia on 26-10-2017.
 */

public class CoinsDetails {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("CoinName")
    @Expose
    private String coinName;
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("Algorithm")
    @Expose
    private String algorithm;
    @SerializedName("ProofType")
    @Expose
    private String proofType;
    @SerializedName("SortOrder")
    @Expose
    private String sortOrder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getProofType() {
        return proofType;
    }

    public void setProofType(String proofType) {
        this.proofType = proofType;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
