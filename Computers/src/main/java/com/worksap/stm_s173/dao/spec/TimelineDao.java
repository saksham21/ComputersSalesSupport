package com.worksap.stm_s173.dao.spec;

import java.io.IOException;
import java.util.List;

import com.worksap.stm_s173.dto.TimelineDto;

public interface TimelineDao {

	void addEvent(TimelineDto timelineDto) throws IOException;

	List<TimelineDto> createOrderTimeline(int id) throws IOException;

	void moveThisTimelineToOld(int client_id, int order_id) throws IOException;

	List<TimelineDto> createPastOrderTimeline(int id) throws IOException;

	void setOrderId(int client_id, int order_id) throws IOException;

}
