package com.example.vtravelwebapp.controller;

import com.example.vtravelwebapp.model.SearchParameters;
import com.example.vtravelwebapp.model.Travel;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class TravelSearchController {

    @GetMapping("/search-travel")
    @ResponseBody
    public ResponseEntity<Object> searchTravel(@RequestParam("destination") String destination,
                                               @RequestParam("daterange") String dateRange,
                                               @RequestParam("numPeople") int numPeople,
                                               @RequestParam(value = "saveSearch", required = false) boolean saveSearch,
                                               HttpSession session,
                                               Model model) {
        // Print form data for testing
        System.out.println("Destination: " + destination);
        System.out.println("Date Range: " + dateRange);
        System.out.println("# of People: " + numPeople);

        // Extract start and end dates from the date range string
        String[] dates = dateRange.split(" - ");
        String startDateString = dates[0];
        String endDateString = dates[1];

        // Parse start and end dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate;
        Date endDate;
        try {
            startDate = dateFormat.parse(startDateString);
            endDate = dateFormat.parse(endDateString);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date range", e);
        }

        // Format start and end dates as strings in the required format
        SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStartDate = apiDateFormat.format(startDate);
        String formattedEndDate = apiDateFormat.format(endDate);

        // Construct the full API URL
        String VOYAGES_API_URL = "http://localhost:8080/api/voyages";
        String fullUrl = VOYAGES_API_URL + "?startDate=" + formattedStartDate + "&endDate=" + formattedEndDate +
                "&destination=" + destination + "&numPeople=" + numPeople;

        // Print the full API URL
        System.out.println("Full API URL: " + fullUrl);
        try {
            // Call the voyages API to retrieve voyages based on the search parameters
            RestTemplate restTemplate = new RestTemplate();
            Travel[] travels = restTemplate.getForObject(fullUrl, Travel[].class);
            List<Travel> voyageList = Arrays.asList(travels);

            for (Travel voyage : voyageList) {
                String destinationName = getDestinationName(voyage.getDestinationId());
                String destinationImage = getImagePath(voyage.getDestinationId());
                voyage.setDestinationName(destinationName);
                voyage.setDestinationImage(destinationImage);
            }

            session.setAttribute("searchvoyages", voyageList); // Storing voyages list in session

            // Check if no voyages were found
            if (voyageList.isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.NO_CONTENT, "No voyage found for the search parameters.");
            }

            // Print the retrieved voyages
            System.out.println("Retrieved Voyages:");
            for (Travel voyage : voyageList) {
                System.out.println(voyage);
            }

            SearchParameters searchParameters = new SearchParameters();
            searchParameters.setDestination(destination);
            searchParameters.setDateRange(dateRange);
            searchParameters.setNumPeople(numPeople);

            // Store the searchParameters object in the session
            session.setAttribute("latestSearchParameters", searchParameters);

            // Save the search parameters if saveSearch is true
            if (saveSearch) {

                // Store the searchParameters object in the session
                session.setAttribute("savedSearchParameters", searchParameters);

                // Print confirmation message
                System.out.println("Search parameters saved.");
                System.out.println("SearchParameters object: " + session.getAttribute("savedSearchParameters"));
            }

            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/travels")).build();

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NO_CONTENT) {
                System.out.println("No Voyage found for the search parameters.");
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/travels")).build();
            } else {
                // Handle other HTTP client errors
                return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
            }
        } catch (RestClientException e) {
            // Handle other RestTemplate errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while calling the API");
        }
    }

    public String getImagePath(Long destinationId) {
        // Make HTTP request to your REST API to fetch the image path for the given destination ID
        RestTemplate restTemplate = new RestTemplate();
        String imagePath = restTemplate.getForObject("http://localhost:8080/api/destinations/" + destinationId + "/image", String.class);
        return imagePath;
    }

    public String getDestinationName(Long destinationId) {
        // Make HTTP request to your REST API to fetch the destination name for the given destination ID
        RestTemplate restTemplate = new RestTemplate();
        String destinationName = restTemplate.getForObject("http://localhost:8080/api/destinations/" + destinationId + "/name", String.class);
        return destinationName;
    }
}
