package multiverse.androidapp.multiverse.model.webModel.commonModel;

import multiverse.androidapp.multiverse.model.commonModel.LocationModel;

public class LocationWebModel {

    public double longitude;
    public double latitude;

    public LocationWebModel() {

    }

    public LocationWebModel(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public LocationModel toCommonModel() {
        LocationModel model = new LocationModel();
        model.longitude = longitude;
        model.latitude = latitude;
        return model;
    }
}
