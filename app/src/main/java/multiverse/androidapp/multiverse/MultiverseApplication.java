package multiverse.androidapp.multiverse;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import multiverse.androidapp.multiverse.database.localDatabase.MultiverseDbHelper;

public class MultiverseApplication extends Application {
    public ExecutorService executorService = Executors.newFixedThreadPool(4);
    public MultiverseDbHelper dbHelper = new MultiverseDbHelper(this);
}
