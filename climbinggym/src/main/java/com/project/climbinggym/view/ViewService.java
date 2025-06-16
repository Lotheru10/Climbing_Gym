package com.project.climbinggym.view;

import com.project.climbinggym.model.TimeSlot;
import com.project.climbinggym.model.User;
import com.project.climbinggym.model.nested.user.Reservation;
import com.project.climbinggym.service.TimeSlotService;
import com.project.climbinggym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ViewService {

    @Autowired
    private UserService userService;

    @Autowired
    private TimeSlotService timeSlotService;

    public SlotReservationsView getTimeSlotReservationsView(LocalDate date) {
        SlotReservationsView view = new SlotReservationsView(date);
        List<User> allUsers = userService.getAllUsers();
        TimeSlot timeSlot = timeSlotService.getOrCreateTimeSlot(date);

        List<SlotReservationsView.ReservationSummary> morningReservations = new ArrayList<>();
        List<SlotReservationsView.ReservationSummary> noonReservations = new ArrayList<>();
        List<SlotReservationsView.ReservationSummary> eveningReservations = new ArrayList<>();

        for (User user : allUsers) {
            for (Reservation reservation : user.getReservations()) {
                if (reservation.getDate().equals(date) && reservation.getStatus() == 'A') {
                    SlotReservationsView.ReservationSummary summary = new SlotReservationsView.ReservationSummary(
                            reservation.getReservationId(),
                            user.getFirstname() + " " + user.getLastname(),
                            user.getId(),
                            reservation.getPeopleAmount(),
                            reservation.getStatus()
                    );

                    switch (reservation.getDayTime().toLowerCase()) {
                        case "morning":
                            morningReservations.add(summary);
                            break;
                        case "noon":
                            noonReservations.add(summary);
                            break;
                        case "evening":
                            eveningReservations.add(summary);
                            break;
                    }
                }
            }
        }

        view.setMorningReservations(morningReservations);
        view.setNoonReservations(noonReservations);
        view.setEveningReservations(eveningReservations);

        view.setMorningStats(calculateSlotStats(timeSlot.getDetails().getMorning().getMaxSlots(),
                morningReservations));
        view.setNoonStats(calculateSlotStats(timeSlot.getDetails().getNoon().getMaxSlots(),
                noonReservations));
        view.setEveningStats(calculateSlotStats(timeSlot.getDetails().getEvening().getMaxSlots(),
                eveningReservations));

        return view;
    }

    public List<SlotReservationsView> getTimeSlotReservationsViewRange(LocalDate startDate, LocalDate endDate) {
        List<SlotReservationsView> views = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            views.add(getTimeSlotReservationsView(currentDate));
            currentDate = currentDate.plusDays(1);
        }

        return views;
    }

    private SlotReservationsView.SlotStats calculateSlotStats(int maxSlots, List<SlotReservationsView.ReservationSummary> reservations) {
        int totalPeople = reservations.stream()
                .mapToInt(SlotReservationsView.ReservationSummary::getPeopleAmount)
                .sum();

        return new SlotReservationsView.SlotStats(maxSlots, totalPeople, reservations.size());
    }
}