package com.example.myapplication.Service;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DirectionsResponse {
    @SerializedName("routes")
    public List<Route> routes;

    public class Route {
        @SerializedName("overview_polyline")
        public OverviewPolyline overviewPolyline;
    }

    public class OverviewPolyline {
        @SerializedName("points")
        public String points;
    }
}
