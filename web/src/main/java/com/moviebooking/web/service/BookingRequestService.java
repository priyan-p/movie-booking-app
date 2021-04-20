package com.moviebooking.web.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviebooking.web.model.Booking;
import com.moviebooking.web.model.BookingRequest;
import com.moviebooking.web.model.ShowSeat;
import com.moviebooking.web.model.Ticket;
import com.moviebooking.web.repository.BlockedSeatRepository;
import com.moviebooking.web.repository.BookedSeatRepository;
import com.moviebooking.web.repository.BookingRequestRepository;
import com.moviebooking.web.repository.SeatAvailabilityRepository;
import com.moviebooking.web.repository.ShowSeatRepository;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingRequestService {

    @Value("${app.booking.mq.routing.key}")
    private String routingKey;

    @Value("${app.booking.maxSeatsAllowed}")
    private int maxSeatsPerUser;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingRequestRepository bookingRequestRepository;

    @Autowired
    private SeatAvailabilityRepository seatAvailabilityRepository;

    @Autowired
    private BlockedSeatRepository blockedSeatRepository;

    @Autowired
    private BookedSeatRepository bookedSeatRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange directExchange;

    /**
     * To add the request to database for concurrency handling
     * 
     * @param booking
     * @return
     * @throws JsonProcessingException
     */
    public BookingRequest addRequest(Booking booking) throws JsonProcessingException {
        BookingRequest requestMessage = new BookingRequest();
        UUID correlationId = UUID.randomUUID();
        requestMessage.setCorrelationId(correlationId);
        requestMessage.setData(objectMapper.writeValueAsString(booking));
        return bookingRequestRepository.save(requestMessage);
    }

    /**
     * To check whether to proceed with this request
     * 
     * @param correlationId
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean validateConcurrentRequests(String correlationId) {
        return bookingRequestRepository.validateConcurrentRequests(correlationId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateProcessStatus(UUID correlationId) {
        bookingRequestRepository.findById(correlationId).ifPresent(b -> {
            b.setProcessed(true);
            bookingRequestRepository.save(b);
        });
    }

    /**
     * To process the request
     * 
     * @param booking
     * @return valid response to http client
     */
    public ResponseEntity<?> process(Booking booking) {
        int userId = 0;
        String response = validate(booking.getShowSeatIds(), userId);
        if (response != null) {
            return ResponseEntity.badRequest().body(response);
        }
        response = template.convertSendAndReceiveAsType(directExchange.getName(), routingKey, booking,
                new ParameterizedTypeReference<String>() {
                });
        return "ok".equals(response) ? ResponseEntity.ok().body(new Ticket(booking.getShowSeatIds()))
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * To validate the request before processing
     * 
     * @param showSeatIds
     * @param userId
     * @return error response or null if not passed
     */
    private String validate(Set<Integer> showSeatIds, int userId) {
        if (showSeatIds.size() > maxSeatsPerUser) {
            return "Maximum seats allowed is " + maxSeatsPerUser;
        }

        List<ShowSeat> showSeatsSelected = (List<ShowSeat>) showSeatRepository.findAllById(showSeatIds);
        boolean allSeatsBelongToSameShow = showSeatsSelected.stream().map(s -> s.getShow().getId()).distinct()
                .count() == 1;
        if (!allSeatsBelongToSameShow) {
            return "Invalid seat selection";
        }

        int showId = showSeatsSelected.get(0).getShow().getId();
        List<ShowSeat> allShowSeats = showSeatRepository.findAllByShowId(showId);

        if (blockedSeatRepository.countByUserId(userId) > 0) {
            return "Duplicate session";
        }

        int prevBookedSeats = bookedSeatRepository.countByUserIdAndShowSeatIn(userId, allShowSeats);
        if (prevBookedSeats + showSeatIds.size() > maxSeatsPerUser) {
            return "Maximum seats allowed is " + maxSeatsPerUser;
        }

        int count = seatAvailabilityRepository.countByIdIn(showSeatIds);
        if (count != showSeatIds.size()) {
            return "Requested seats not available";
        }
        return null;
    }
}
