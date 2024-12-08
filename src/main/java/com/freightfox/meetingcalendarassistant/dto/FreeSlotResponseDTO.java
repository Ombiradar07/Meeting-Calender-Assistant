package com.freightfox.meetingcalendarassistant.dto;


import java.time.LocalDateTime;

public class FreeSlotResponseDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public FreeSlotResponseDTO(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
