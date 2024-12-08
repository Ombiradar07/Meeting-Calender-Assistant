package com.freightfox.meetingcalendarassistant.service;

import com.freightfox.meetingcalendarassistant.dto.*;

import java.util.List;

public interface MeetingService {
    MeetingDTO createNewMeeting(MeetingRequestDTO meetingRequestDTO);

    ConflictResponseDTO checkForConflicts(MeetingRequestDTO meetingRequestDTO);

    List<FreeSlotResponseDTO> findFreeSlots(FreeSlotRequestDTO freeSlotRequestDTO);

}
