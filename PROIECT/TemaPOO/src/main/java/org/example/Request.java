package org.example;

import java.time.LocalDateTime;

public class Request {

    private final RequestType requestType;
    private final LocalDateTime creationDate;
    private final String description;
    private final String requestingUser;
    private final String assignedUser;
    private String actorName;
    private String movieTitle;

    private Request(RequestType requestType, String description,
                    String requestingUser, String assignedUser, LocalDateTime creationDate) {
        this.requestType = requestType;
        this.creationDate = creationDate;
        this.description = description;
        this.requestingUser = requestingUser;
        this.assignedUser = assignedUser;
    }

    public static Request createGenericRequest(RequestType requestType, String description,
                                               String requestingUser, String assignedUser, LocalDateTime creationDate) {
        return new Request(requestType, description, requestingUser, assignedUser, creationDate);
    }

    public static Request createActorIssue(RequestType requestType, String description,
                                           String requestingUser, String assignedUser, LocalDateTime creationDate,
                                           String actorName) {
        Request request = new Request(requestType, description, requestingUser, assignedUser, creationDate);
        request.actorName = actorName;
        return request;
    }

    public static Request createMovieIssue(RequestType requestType, String description,
                                           String requestingUser, String assignedUser, LocalDateTime creationDate,
                                           String movieTitle) {
        Request request = new Request(requestType, description, requestingUser, assignedUser, creationDate);
        request.movieTitle = movieTitle;
        return request;
    }


    public String getDescription() {
        return description;
    }

}
