package gateway.core;

/**
 * Created by snow_young on 16/3/23.
 */
public enum SessionState {
    PRE_FILTER, CREATE_CONN, SEND_REQUEST, POST_FILTER, SEND_RESPONSE, QUIT
}
