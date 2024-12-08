package com.freightfox.meetingcalendarassistant.controller;

import com.freightfox.meetingcalendarassistant.dto.*;
import com.freightfox.meetingcalendarassistant.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meetings")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    // Endpoint to create a new meeting
    @PostMapping("/create")
    public ResponseEntity<MeetingDTO> createMeeting(@RequestBody MeetingRequestDTO meetingRequestDTO) {
        MeetingDTO createdMeeting = meetingService.createNewMeeting(meetingRequestDTO);

        return new ResponseEntity<>(createdMeeting, HttpStatus.CREATED);
    }

    // Endpoint to check for meeting conflicts
    @PostMapping("/check-conflicts")
    public ResponseEntity<ConflictResponseDTO> checkForConflicts(@RequestBody MeetingRequestDTO meetingRequestDTO) {
        ConflictResponseDTO conflictResponse = meetingService.checkForConflicts(meetingRequestDTO);
        return ResponseEntity.ok(conflictResponse);
    }

    // Endpoint to find free slots for two employees
    @PostMapping("/free-slots")
    public ResponseEntity<List<FreeSlotResponseDTO>> findFreeSlots(@RequestBody FreeSlotRequestDTO freeSlotRequestDTO) {
        List<FreeSlotResponseDTO> freeSlots = meetingService.findFreeSlots(freeSlotRequestDTO);
        return ResponseEntity.ok(freeSlots);
    }
}
