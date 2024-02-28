package org.example;

import java.util.ArrayList;
import java.util.List;

public class RequestsHolder {
    private static List<Request> requests = new ArrayList<>();

    private RequestsHolder() {
    }

    // Static method to add a request
    public static void addRequest(Request request) {
        requests.add(request);
    }

    // Static method to remove a request
    public static void removeRequest(Request request) {
        requests.remove(request);
    }

    public static boolean requestExist(Request request) {
        for (Request item : requests) {
            if (item.equals(request))
                return true;
        }
        return false;
    }

    public static List<Request> getRequests() {
        return requests;
    }
}