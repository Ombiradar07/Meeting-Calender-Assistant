package com.freightfox.meetingcalendarassistant.repository;

import com.freightfox.meetingcalendarassistant.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByOwnerId(Long id);

}
