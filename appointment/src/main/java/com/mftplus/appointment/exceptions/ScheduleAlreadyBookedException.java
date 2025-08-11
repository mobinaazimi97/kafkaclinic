package com.mftplus.appointment.exceptions;

public class ScheduleAlreadyBookedException extends RuntimeException {
    public ScheduleAlreadyBookedException(String message) {
        super(message);
    }
}
