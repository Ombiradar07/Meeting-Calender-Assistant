package com.freightfox.meetingcalendarassistant.dto;


import java.time.LocalDateTime;

public class FreeSlotRequestDTO {
    private Long employee1Id;
    private Long employee2Id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int duration; // in minutes

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Long getEmployee1Id() {
        return employee1Id;
    }

    public void setEmployee1Id(Long employee1Id) {
        this.employee1Id = employee1Id;
    }

    public Long getEmployee2Id() {
        return employee2Id;
    }

    public void setEmployee2Id(Long employee2Id) {
        this.employee2Id = employee2Id;
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
