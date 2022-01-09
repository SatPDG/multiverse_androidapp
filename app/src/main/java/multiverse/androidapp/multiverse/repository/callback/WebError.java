package multiverse.androidapp.multiverse.repository.callback;

import multiverse.androidapp.multiverse.database.webDatabase.WebServiceResponse;

public class WebError {

    public boolean authenticationError;
    public boolean ressourceNotAccessible;
    public boolean serverError;
    public boolean isOffline;

    public WebError(WebServiceResponse<?> response) {
        this.authenticationError = response.authenticationError;
        this.ressourceNotAccessible = response.isRessourceNotAccessible;
        this.serverError = response.serverError;
        this.isOffline = response.isOffline;
    }
}
