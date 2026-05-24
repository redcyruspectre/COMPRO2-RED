package com.redcyrus;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    protected double timepoint;
    
    @SerializedName("temp2m")
    protected double temperature;
    
    @SerializedName("wind10m")
    protected Wind wind;
}