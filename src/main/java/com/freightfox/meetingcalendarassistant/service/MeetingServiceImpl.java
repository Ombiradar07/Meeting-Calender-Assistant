package com.freightfox.meetingcalendarassistant.service;


import com.freightfox.meetingcalendarassistant.dto.*;
import com.freightfox.meetingcalendarassistant.entity.Employee;
import com.freightfox.meetingcalendarassistant.entity.Meeting;
import com.freightfox.meetingcalendarassistant.exception.MeetingConflictException;
import com.freightfox.meetingcalendarassistant.exception.PastTimeException;
import com.freightfox.meetingcalendarassistant.repository.EmployeeRepository;
import com.freightfox.meetingcalendarassistant.repository.MeetingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public MeetingServiceImpl(MeetingRepository meetingRepository, EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.meetingRepository = meetingRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }



    @Override
    public MeetingDTO createNewMeeting(MeetingRequestDTO meetingRequestDTO) {
        // Step 1: Validate past time
        validatePastTime(meetingRequestDTO.getStartTime());

        // Step 2: Validate conflicts
        validateMeetingConflicts(meetingRequestDTO);

        // Step 3: Fetch the meeting owner
        Employee owner = getEmployee(meetingRequestDTO.getOwnerId());

        // Step 4: Fetch the participants
        List<Employee> participants = getParticipants(meetingRequestDTO.getParticipantIds());

        // Step 5: Create and persist the meeting
        Meeting meeting = createMeeting(meetingRequestDTO, owner, participants);
        Meeting savedMeeting = meetingRepository.save(meeting);

        // Step 6: Map savedMeeting to MeetingDTO
        MeetingDTO meetingDTO = modelMapper.map(savedMeeting, MeetingDTO.class);

        // Manually map participantIds
        List<Long> participantIds = savedMeeting.getParticipants().stream()
                .map(Employee::getId)
                .collect(Collectors.toList());
        meetingDTO.setParticipantIds(participantIds);

        return meetingDTO;
    }

    // Utility method to validate past time
    private void validatePastTime(LocalDateTime startTime) {
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new PastTimeException("Meetings cannot be booked for past times.");
        }
    }



    @Override
    public ConflictResponseDTO checkForConflicts(MeetingRequestDTO meetingRequestDTO) {
        boolean hasConflict = false;
        List<Long> conflictingParticipants = new ArrayList<>();

        // Step 1: Get all meetings (owner + participants)
        List<Meeting> conflictingMeetings = findConflictingMeetings(meetingRequestDTO);

        // Step 2: Check for conflicts in owner and participant meetings
        for (Meeting meeting : conflictingMeetings) {
            if (isConflicting(meeting, meetingRequestDTO.getStartTime(), meetingRequestDTO.getEndTime())) {
                conflictingParticipants.add(meeting.getOwner().getId());
                hasConflict = true;
            }
        }

        // Step 3: Return response with conflict status and participant details
        return new ConflictResponseDTO(hasConflict, conflictingParticipants);
    }

    @Override
    public List<FreeSlotResponseDTO> findFreeSlots(FreeSlotRequestDTO requestDTO) {
        // Step 1: Fetch all meetings for both employees
        List<Meeting> combinedMeetings = fetchCombinedMeetings(requestDTO.getEmployee1Id(), requestDTO.getEmployee2Id());

        // Step 2: Calculate available free slots
        return calculateFreeSlots(combinedMeetings, requestDTO);
    }

    // Utility method to validate if the requested meeting time conflicts with existing meetings
    private void validateMeetingConflicts(MeetingRequestDTO meetingRequestDTO) {
        ConflictResponseDTO conflictResponse = checkForConflicts(meetingRequestDTO);
        if (conflictResponse.isConflict()) {
            throw new MeetingConflictException("Meeting conflicts with existing schedules.");
        }
    }

    // Utility method to find all meetings for the owner and participants
    private List<Meeting> findConflictingMeetings(MeetingRequestDTO meetingRequestDTO) {
        List<Meeting> ownerMeetings = meetingRepository.findByOwnerId(meetingRequestDTO.getOwnerId());
        List<Meeting> participantMeetings = new ArrayList<>();

        for (Long participantId : meetingRequestDTO.getParticipantIds()) {
            participantMeetings.addAll(meetingRepository.findByOwnerId(participantId));
        }

        // Combine all meetings
        List<Meeting> allMeetings = new ArrayList<>();
        allMeetings.addAll(ownerMeetings);
        allMeetings.addAll(participantMeetings);

        return allMeetings;
    }

    // Utility method to check if the meeting conflicts with existing meetings based on time
    private boolean isConflicting(Meeting meeting, LocalDateTime startTime, LocalDateTime endTime) {
        return startTime.isBefore(meeting.getEndTime()) && endTime.isAfter(meeting.getStartTime());
    }

    // Utility method to fetch a single employee by their ID
    private Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
    }

    // Utility method to fetch a list of participants based on their IDs
    private List<Employee> getParticipants(List<Long> participantIds) {
        return participantIds.stream()
                .map(this::getEmployee)
                .collect(Collectors.toList());
    }


    // Utility method to create the meeting entity
    private Meeting createMeeting(MeetingRequestDTO meetingRequestDTO, Employee owner, List<Employee> participants) {
        Meeting meeting = new Meeting();
        meeting.setStartTime(meetingRequestDTO.getStartTime());
        meeting.setEndTime(meetingRequestDTO.getEndTime());
        meeting.setOwner(owner);
        meeting.setParticipants(participants);
        return meeting;
    }


    // Utility method to fetch meetings for both employees and combine them
    private List<Meeting> fetchCombinedMeetings(Long employee1Id, Long employee2Id) {
        List<Meeting> employee1Meetings = meetingRepository.findByOwnerId(employee1Id);
        List<Meeting> employee2Meetings = meetingRepository.findByOwnerId(employee2Id);

        // Combine both employee meeting lists and sort by start time
        List<Meeting> combinedMeetings = new ArrayList<>();
        combinedMeetings.addAll(employee1Meetings);
        combinedMeetings.addAll(employee2Meetings);
        combinedMeetings.sort(Comparator.comparing(Meeting::getStartTime));

        return combinedMeetings;
    }

    // Utility method to calculate free slots based on the combined meetings
    private List<FreeSlotResponseDTO> calculateFreeSlots(List<Meeting> meetings, FreeSlotRequestDTO requestDTO) {
        List<FreeSlotResponseDTO> freeSlots = new ArrayList<>();
        LocalDateTime currentStart = requestDTO.getStartTime();

        // Loop through the meetings to find gaps
        for (Meeting meeting : meetings) {
            // Check if there is enough time between the current free start time and the next meeting's start time
            if (currentStart.plusMinutes(requestDTO.getDuration()).isBefore(meeting.getStartTime())) {
                freeSlots.add(new FreeSlotResponseDTO(currentStart, meeting.getStartTime()));
            }
            // Update current start time to the end time of the last meeting if it is later
            currentStart = meeting.getEndTime().isAfter(currentStart) ? meeting.getEndTime() : currentStart;
        }

        // Add remaining available time after the last meeting if possible
        if (currentStart.plusMinutes(requestDTO.getDuration()).isBefore(requestDTO.getEndTime())) {
            freeSlots.add(new FreeSlotResponseDTO(currentStart, requestDTO.getEndTime()));
        }

        return freeSlots;
    }

}
