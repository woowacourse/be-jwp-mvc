package nextstep.web.support;

public enum HttpStatus {
    OK(200, "OK"),
    CREATED(201, "Created"),
    FOUND(302, "Found"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    NOT_FOUND(404, "Not Found");

    private final int statusCode;
    private final String statusName;

    HttpStatus(final int statusCode, final String statusName) {
        this.statusCode = statusCode;
        this.statusName = statusName;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusName() {
        return statusName;
    }
}
