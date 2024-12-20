package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RequestsHolder {
    private static List<Request> allRequests = new ArrayList<>();

    private RequestsHolder() {
        // Do nothing, as this class is intended to be used statically
    }
    public static void addRequest(Request request) {
        allRequests.add(request);
    }
    public static void removeRequest(Request request) {
        allRequests.remove(request);
    }
    public static List<Request> getAllRequests() {
        return Collections.unmodifiableList(allRequests);
    }
}

