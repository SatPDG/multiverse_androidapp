package multiverse.androidapp.multiverse.model.commonModel;

import multiverse.androidapp.multiverse.model.webModel.commonModel.LocationWebModel;

public class LocationModel {

    public double longitude;
    public double latitude;

    public LocationModel() {

    }

    public LocationWebModel toWebModel() {
        LocationWebModel webModel = new LocationWebModel();
        webModel.longitude = longitude;
        webModel.latitude = latitude;
        return webModel;
    }
}
