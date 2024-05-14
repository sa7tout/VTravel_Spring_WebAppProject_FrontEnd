package com.example.vtravelwebapp.controller;

import com.example.vtravelwebapp.model.Reservation;
import com.example.vtravelwebapp.model.Travel;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Controller
public class ReservationController {

    private final String VALIDATE_SESSION_URL = "http://localhost:8080/validate-session";
    private final String VOYAGE_API_URL = "http://localhost:8080/api/voyages/";
    private final String DESTINATION_API_URL = "http://localhost:8080/api/destinations/";
    private final String BOOKING_API_URL = "http://localhost:8080/api/bookings";
    private final String USER_API_URL = "http://localhost:8080/api/customers/user";



    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HttpSession session;

    @PostMapping("/travels/booking")
    public String bookTravel(@RequestParam("voyageId") Long voyageId,
                             @RequestParam("sessionToken") String sessionToken,
                             @RequestParam("destination") String destination,
                             @RequestParam("dateRange") String dateRange,
                             @RequestParam("numPeople") String numPeople,
                             Model model) {
        // Validate session
        ResponseEntity<String> sessionValidationResponse = validateSession(sessionToken);
        if (!sessionValidationResponse.getStatusCode().is2xxSuccessful()) {
            // If session is not valid, redirect to sign-in page or display error message
            return "redirect:/index"; // Example: redirect to sign-in page
        }
        System.out.println("sessiontoken:"+sessionToken);

        session.setAttribute("sessionToken", sessionToken);

        // Add search parameters to model
        model.addAttribute("destination", destination);
        model.addAttribute("dateRange", dateRange);
        model.addAttribute("numPeople", numPeople);
        model.addAttribute("hotel", "VAGO Hotel");
        model.addAttribute("", destination);

        // Call API to retrieve voyage by ID
        ResponseEntity<Travel> responseEntity = getVoyageById(voyageId);

        // Check if the API call was successful
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // Retrieve the Travel object from the response body
            Travel travel = responseEntity.getBody();
            System.out.println(travel);

            // Add the retrieved voyage to the model
            model.addAttribute("selectedVoyage", travel);
            model.addAttribute("pricePerPerson", travel.getPrice());
            BigDecimal totalPrice = travel.getPrice().multiply(BigDecimal.valueOf(Long.parseLong(numPeople)));
            model.addAttribute("totalPrice", totalPrice);

            // Get actual local date
            LocalDate bookingLocalDate = LocalDate.now();

            // Prepare the request body
            Reservation booking = new Reservation();
            booking.setVoyageId(voyageId);
            booking.setBookingDate(Date.from(bookingLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            booking.setTotalPrice(totalPrice);
            booking.setStatus("PENDING");

            session.setAttribute("Booking", booking);

            // Call API to retrieve destination name and image path
            ResponseEntity<String> nameResponseEntity = getDestinationNameById(Integer.parseInt(destination));
            ResponseEntity<String> imagePathResponseEntity = getDestinationImagePathById(Integer.parseInt(destination));

            if (nameResponseEntity.getStatusCode().is2xxSuccessful() && imagePathResponseEntity.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("destinationName", nameResponseEntity.getBody());
                model.addAttribute("destinationImagePath", imagePathResponseEntity.getBody());
            } else {
                // Handle the case where the destination details retrieval failed
                model.addAttribute("errorMessage", "Failed to retrieve destination details.");
            }

        } else {
            // Handle the case where the voyage retrieval failed
            // For example, display an error message or redirect to an error page
            model.addAttribute("errorMessage", "Failed to retrieve voyage details.");
        }

        // Return the name of the Thymeleaf template for the booking page
        return "booking";
    }


    // Method to call the API to retrieve a voyage by its ID
    private ResponseEntity<Travel> getVoyageById(Long voyageId) {
        // Make GET request to retrieve voyage by ID
        ResponseEntity<Travel> responseEntity = restTemplate.getForEntity(VOYAGE_API_URL + voyageId, Travel.class);
        return responseEntity;
    }

    // Method to validate session
    private ResponseEntity<String> validateSession(String sessionToken) {
        // Make GET request to validate session
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(VALIDATE_SESSION_URL, String.class);
        return responseEntity;
    }
    // Method to call the API to retrieve destination name by its ID
    private ResponseEntity<String> getDestinationNameById(int destinationId) {
        // Make GET request to retrieve destination name by ID
        return restTemplate.getForEntity(DESTINATION_API_URL + destinationId + "/name", String.class);
    }

    // Method to call the API to retrieve destination image path by its ID
    private ResponseEntity<String> getDestinationImagePathById(int destinationId) {
        // Make GET request to retrieve destination image path by ID
        return restTemplate.getForEntity(DESTINATION_API_URL + destinationId + "/image", String.class);
    }
    @PostMapping("/travels/booking/confirm_booking")
    public String completeBooking(Model model) {
        try {
            // Retrieve necessary attributes from the model
            Long customerId;


            // Prepare the request body
            Reservation booking = (Reservation) session.getAttribute("Booking");


            String sessionToken = (String) session.getAttribute("sessionToken");

            // Check if session token is present
            if (sessionToken == null) {
                // Handle missing session token error
                System.out.println("Session token is missing.");
                return "bookingfailure"; // Redirect to failure page or return error message
            }

            // Call the user API to get the customer details using the session token
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", (String) session.getAttribute("sessionToken"));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(USER_API_URL, HttpMethod.GET, entity, String.class);
            System.out.println("User API Response Body: " + response.getBody());
            if (response.getStatusCode() == HttpStatus.OK) {
                // Parse JSON response to get customer ID
                JSONObject jsonResponse = new JSONObject(response.getBody());
                customerId = jsonResponse.getLong("customerId");
                booking.setCustomerId(customerId);
            }

            // Send POST request to create the booking
            ResponseEntity<Reservation> responseEntity = restTemplate.postForEntity(BOOKING_API_URL, booking, Reservation.class);


            if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("success:");
                // Booking successfully created
                return "bookingconfirmation"; // Redirect to success page or return success message
            } else {
                System.out.println("Failed to create booking. Status code: " + responseEntity.getStatusCodeValue());
                return "bookingfailure"; // Redirect to failure page or return error message
            }
        } catch (Exception e) {
            System.out.println("Error in completeBooking method:" + e);
            return "bookingfailure";
        }
    }



}
