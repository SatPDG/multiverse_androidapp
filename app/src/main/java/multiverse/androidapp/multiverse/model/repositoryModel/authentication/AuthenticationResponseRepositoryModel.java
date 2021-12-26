package multiverse.androidapp.multiverse.model.repositoryModel.authentication;

public class AuthenticationResponseRepositoryModel {

    public AuthenticationCode code;
    public int userID;
    public String token;
    public String refreshToken;

    public enum AuthenticationCode {
        OK, BAD_CREDENTIAL, EMAIL_ALREADY_IN_USE, UNAUTHORIZE, REFRESH_TOKEN_EXPIRE, SERVER_ERROR
    }

}
