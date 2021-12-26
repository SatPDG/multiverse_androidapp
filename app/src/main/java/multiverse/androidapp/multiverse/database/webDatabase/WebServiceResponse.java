package multiverse.androidapp.multiverse.database.webDatabase;

public class WebServiceResponse<T> {

    public int httpCode;
    public int apiCode;
    public T data;

    public boolean isResponseOK;
    public boolean isRessourceNotAccessible;
    public boolean authenticationError;
    public boolean serverError;
    public boolean isOffline;
}
