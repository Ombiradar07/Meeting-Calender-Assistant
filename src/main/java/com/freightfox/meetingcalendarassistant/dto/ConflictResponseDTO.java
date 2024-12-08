package com.freightfox.meetingcalendarassistant.dto;

import java.util.List;


public class ConflictResponseDTO {
    private boolean conflict;
    private List<Long> conflictingParticipantIds;

    public ConflictResponseDTO(boolean conflict, List<Long> conflictingParticipantIds) {
        this.conflict = conflict;
        this.conflictingParticipantIds = conflictingParticipantIds;
    }

    public boolean isConflict() {
        return conflict;
    }

    public void setConflict(boolean conflict) {
        this.conflict = conflict;
    }

    public List<Long> getConflictingParticipantIds() {
        return conflictingParticipantIds;
    }

    public void setConflictingParticipantIds(List<Long> conflictingParticipantIds) {
        this.conflictingParticipantIds = conflictingParticipantIds;
    }
}
