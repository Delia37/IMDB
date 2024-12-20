package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class IMDB<T> {
    private final List<User> users;
    private final List<Actor> actors;
    private final List<Request> requests;
    private final List<Production> productions;
    private final Scanner scanner;

    public IMDB() {
        users = new ArrayList<>();
        actors = new ArrayList<>();
        requests = new ArrayList<>();
        productions = new ArrayList<>();
        scanner = new Scanner(System.in);
    }
    public void run() {
        System.out.println("Choose interface mode:");
        System.out.println("1. Terminal");
        System.out.println("2. Graphic");
        int interfaceChoice = scanner.nextInt();

        if (interfaceChoice == 1) {
            terminalFlow();
        } else if (interfaceChoice == 2) {
            System.out.println("Graphical interface is not implemented yet.");
        } else {
            System.out.println("Invalid choice. Exiting...");
        }
    }


    private void terminalFlow() {
        loadFromJsonFiles();

        User loggedInUser = null;

        while (true) {
            if (loggedInUser == null) {
                System.out.println("Login:");
                System.out.print("Enter your email: ");
                String email = scanner.next();
                System.out.print("Enter your password: ");
                String password = scanner.next();

                loggedInUser = login(email, password);

                if (loggedInUser != null) {
                    System.out.println("Login successful. Welcome, " + loggedInUser.getUsername() + "!");
                    System.out.println();
                } else {
                    System.out.println("Login failed. Invalid email or password. Try again.");
                }
            } else {
                loggedInUser.displayMenu(scanner, productions, actors);

                System.out.print("Do you want to log out? (y/n): ");
                String logoutChoice = scanner.next().toLowerCase();
                if (logoutChoice.equals("y")) {
                    loggedInUser = null;
                }
            }
        }
    }
    private User login(String email, String password) {
        for (User user : users) {
            if (user.getInformation().getCredentials().getEmail().equals(email) &&
                    user.getInformation().getCredentials().getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private void loadFromJsonFiles() {
        try {
            readAccountsJson("E:\\Programare_orientata\\TemaPOO\\src\\test\\resources\\testResources\\" +
                   "accounts.json");
            readActorsJson("E:\\Programare_orientata\\TemaPOO\\src\\test\\resources\\testResources\\" +
                    "actors.json");
            readProductionsJson("E:\\Programare_orientata\\TemaPOO\\src\\test\\resources\\testResources\\" +
                    "production.json");
            readRequestsJson("E:\\Programare_orientata\\TemaPOO\\src\\test\\resources\\testResources\\" +
                    "requests.json");
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            System.err.println("Error loading JSON file: " + e.getMessage());
        }
    }

    //CITIRE USERS
    private void readAccountsJson(String filePath) throws IOException, org.json.simple.parser.ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filePath));
        JSONArray jsonArray = (JSONArray) obj;

        for (Object jsonElement : jsonArray) {
            JSONObject accountJson = (JSONObject) jsonElement;
            User user = createUserFromJson(accountJson);
            users.add(user);
        }
    }

    private User createUserFromJson(JSONObject accountJson) {
        String userType = (String) accountJson.get("userType");
        User.Information information = convertJsonToInformation(accountJson);

        switch (userType) {
            case "Regular":
                return createRegularUser(accountJson, information);
            case "Contributor":
                return createContributorUser(accountJson, information);
            case "Admin":
                return createAdminUser(accountJson, information);
            default:
                throw new IllegalArgumentException("Invalid user type");
        }
    }

    private User.Information convertJsonToInformation(JSONObject accountJson) {
        JSONObject credentialsJson = (JSONObject) accountJson.get("information");
        String name = (String) credentialsJson.get("name");
        String country = (String) credentialsJson.get("country");
        int age = ((Long) credentialsJson.get("age")).intValue();
        String gender = (String) credentialsJson.get("gender");
        LocalDate birthDate = LocalDate.parse((String) credentialsJson.get("birthDate"));

        Credentials credentials = convertJsonToCredentials(credentialsJson);

        return new User.Information.InformationBuilder(credentials)
                .name(name)
                .country(country)
                .age(age)
                .gender(gender)
                .birthDate(birthDate)
                .build();
    }

    private Credentials convertJsonToCredentials(JSONObject credentialsJson) {
        JSONObject credentialsObject = (JSONObject) credentialsJson.get("credentials");
        if (credentialsObject != null) {
            String email = (String) credentialsObject.get("email");
            String password = (String) credentialsObject.get("password");

            return new Credentials(email, password);
        } else {
            System.out.println("Credentials object is null.");
            return null;
        }
    }


    private Regular createRegularUser(JSONObject accountJson, User.Information information) {
        String username = (String) accountJson.get("username");
        int experience = Integer.parseInt(accountJson.get("experience").toString());
        List<String> notifications = createNotifications(accountJson);
        SortedSet<Object> favorites = createFavorites(accountJson);

        return new Regular(information, username, experience, notifications, favorites);
    }

    private List<String> createNotifications(JSONObject accountJson) {
        JSONArray notificationsArray = (JSONArray) accountJson.get("notifications");

        List<String> notifications = new ArrayList<>();
        if (notificationsArray != null) {
            notifications.addAll(notificationsArray);
        }

        return notifications;
    }

    private Contributor createContributorUser(JSONObject accountJson, User.Information information) {
        String username = (String) accountJson.get("username");
        int experience = Integer.parseInt(accountJson.get("experience").toString());
        List notifications = (List) accountJson.get("notifications");
        SortedSet<Object> favorites = createFavorites(accountJson);

        List<String> productionsContribution = (List<String>) accountJson.get("productionsContribution");
        List<String> actorsContribution = (List<String>) accountJson.get("actorsContribution");

        Contributor contributor = new Contributor(information, username, experience, (List) notifications, favorites);
        contributor.setProductionsContribution(productionsContribution);
        contributor.setActorsContribution(actorsContribution);

        return contributor;
    }

    private Admin createAdminUser(JSONObject accountJson, User.Information information) {
        String username = (String) accountJson.get("username");
        List notifications = (List) accountJson.get("notifications");
        SortedSet<Object> favorites = createFavorites(accountJson);

        List<String> productionsContribution = (List<String>) accountJson.get("productionsContribution");
        List<String> actorsContribution = (List<String>) accountJson.get("actorsContribution");

        Admin admin = new Admin(information, username, 0, (List) notifications, favorites);
        admin.setProductionsContribution(productionsContribution);
        admin.setActorsContribution(actorsContribution);

        return admin;
    }

    private SortedSet<Object> createFavorites(JSONObject accountJson) {
        JSONArray favoriteProductions = (JSONArray) accountJson.get("favoriteProductions");
        JSONArray favoriteActors = (JSONArray) accountJson.get("favoriteActors");

        SortedSet<Object> favorites = new TreeSet<>();

        if (favoriteProductions != null) {
            favorites.addAll(favoriteProductions);
        }

        if (favoriteActors != null) {
            favorites.addAll(favoriteActors);
        }

        return favorites;
    }

    //CITIRE ACTORI

    private void readActorsJson(String filePath) throws IOException, org.json.simple.parser.ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filePath));
        JSONArray jsonArray = (JSONArray) obj;

        for (Object jsonElement : jsonArray) {
            JSONObject actorJson = (JSONObject) jsonElement;
            Actor actor = createActorFromJson(actorJson);
            actors.add(actor);
        }
    }

    private Actor createActorFromJson(JSONObject actorJson) {
        String name = (String) actorJson.get("name");
        String biography = (String) actorJson.get("biography");

        JSONArray performancesArray = (JSONArray) actorJson.get("performances");
        List<Map.Entry<String, String>> performances = createPerformancesList(performancesArray);

        return new Actor(name, performances, biography);
    }

    private List<Map.Entry<String, String>> createPerformancesList(JSONArray performancesArray) {
        List<Map.Entry<String, String>> performances = new ArrayList<>();

        if (performancesArray != null) {
            for (Object performanceElement : performancesArray) {
                JSONObject performanceJson = (JSONObject) performanceElement;
                String title = (String) performanceJson.get("title");
                String type = (String) performanceJson.get("type");
                performances.add(new AbstractMap.SimpleEntry<>(title, type));
            }
        }

        return performances;
    }

    //CITIRE PRODUCTIONS
    private void readProductionsJson(String filePath) throws IOException, org.json.simple.parser.ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filePath));
        JSONArray jsonArray = (JSONArray) obj;

        for (Object jsonElement : jsonArray) {
            JSONObject productionJson = (JSONObject) jsonElement;
            Production production = createProductionFromJson(productionJson, actors);
            productions.add(production);
        }
    }

    private Production createProductionFromJson(JSONObject productionJson, List<Actor> actors) {
        String title = (String) productionJson.get("title");
        String type = (String) productionJson.get("type");
        List<String> directors = (List<String>) productionJson.get("directors");
        JSONArray actorsArray = (JSONArray) productionJson.get("actors");
        List<Genre> genres = createGenresList((JSONArray) productionJson.get("genres"));
        List<Rating> ratings = createRatingsList((JSONArray) productionJson.get("ratings"));
        String description = (String) productionJson.get("plot");

        if (actorsArray != null && !actorsArray.isEmpty()) {
            for (Object actorElement : actorsArray) {
                String actorName = (String) actorElement;
                // Check if the actor is not already in the actors list
                if (actors.stream().noneMatch(actor -> actor.getName().equals(actorName))) {
                    // If not, create a new actor and add it to the list
                    Actor newActor = new Actor(actorName, new ArrayList<>(), "");
                    actors.add(newActor);
                }
            }
        }


        Production production;
        if ("Movie".equals(type)) {
            int duration = parseDuration((String) productionJson.get("duration"));
            int releaseYear = (productionJson.get("releaseYear") != null) ? Integer.parseInt(productionJson.get("releaseYear").toString()) : 0;
            production = new Movie(title, directors, actorsArray, genres, ratings, description, duration, releaseYear);
        } else if ("Series".equals(type)) {
            int releaseYear = (productionJson.get("releaseYear") != null) ? Integer.parseInt(productionJson.get("releaseYear").toString()) : 0;
            int numberOfSeasons = ((Long) productionJson.get("numSeasons")).intValue();
            Map<String, List<Episode>> seasons = createSeasonsMap((JSONObject) productionJson.get("seasons"));
            production = new Series(title, directors, actorsArray, genres, ratings, description, releaseYear, numberOfSeasons, seasons);
        } else {
            throw new IllegalArgumentException("Invalid production type");
        }

        return production;
    }

    private List<Genre> createGenresList(JSONArray genresArray) {
        List<Genre> genres = new ArrayList<>();

        if (genresArray != null) {
            for (Object genreElement : genresArray) {
                String genre = (String) genreElement;
                genres.add(Genre.valueOf(genre));
            }
        }

        return genres;
    }

    private List<Rating> createRatingsList(JSONArray ratingsArray) {
        List<Rating> ratings = new ArrayList<>();

        if (ratingsArray != null) {
            for (Object ratingElement : ratingsArray) {
                JSONObject ratingJson = (JSONObject) ratingElement;
                String username = (String) ratingJson.get("username");
                int score = ((Long) ratingJson.get("rating")).intValue();
                String comments = (String) ratingJson.get("comment");
                ratings.add(new Rating(username, score, comments));
            }
        }

        return ratings;
    }

    private int parseDuration(String duration) {
        return Integer.parseInt(duration.split(" ")[0]);
    }

    private Map<String, List<Episode>> createSeasonsMap(JSONObject seasonsJson) {
        Map<String, List<Episode>> seasonsMap = new LinkedHashMap<>();

        if (seasonsJson != null) {
            for (Object seasonKey : seasonsJson.keySet()) {
                String seasonName = (String) seasonKey;
                JSONArray episodesArray = (JSONArray) seasonsJson.get(seasonKey);

                List<Episode> episodes = new ArrayList<>();

                if (episodesArray != null) {
                    for (Object episodeElement : episodesArray) {
                        JSONObject episodeJson = (JSONObject) episodeElement;
                        String episodeName = (String) episodeJson.get("episodeName");
                        int duration = parseDuration((String) episodeJson.get("duration"));

                        Episode episode = new Episode(episodeName, duration);
                        episodes.add(episode);
                    }
                }

                seasonsMap.put(seasonName, episodes);
            }
        }

        return seasonsMap;
    }

    //CITIRE REQUESTS
    private void readRequestsJson(String filePath) throws IOException, org.json.simple.parser.ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filePath));
        JSONArray jsonArray = (JSONArray) obj;

        for (Object jsonElement : jsonArray) {
            JSONObject requestJson = (JSONObject) jsonElement;
            Request request = createRequestFromJson(requestJson);
            requests.add(request);
        }
    }

    private Request createRequestFromJson(JSONObject requestJson) {
        String typeString = (String) requestJson.get("type");
        RequestType requestType;
        try {
            requestType = RequestType.valueOf(typeString.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            requestType = RequestType.OTHERS;
        }

        String title = (String) requestJson.get("title");
        String description = (String) requestJson.get("description");
        String requestingUser = (String) requestJson.get("username");
        String assignedUser = (String) requestJson.get("to");

        // Convert creationDate from String to LocalDateTime
        String creationDateString = (String) requestJson.get("createdDate");
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime creationDate = LocalDateTime.parse(creationDateString, formatter);

        switch (requestType) {
            case ACTOR_ISSUE:
                String actorName = (String) requestJson.get("actorName");
                return Request.createActorIssue(requestType, description, requestingUser, assignedUser, creationDate, actorName);
            case MOVIE_ISSUE:
                String movieTitle = (String) requestJson.get("movieTitle");
                return Request.createMovieIssue(requestType, description, requestingUser, assignedUser, creationDate, movieTitle);
            default:
                return Request.createGenericRequest(requestType, description, requestingUser, assignedUser, creationDate);
        }
    }



    public static void main(String[] args) {
        IMDB imdb = new IMDB();
        imdb.run();
    }
}