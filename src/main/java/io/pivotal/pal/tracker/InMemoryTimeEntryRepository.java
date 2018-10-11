package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private Map<Long, TimeEntry> timeEntryMap;

    public InMemoryTimeEntryRepository() {
        timeEntryMap = new HashMap<>();
    }

    public TimeEntry create(TimeEntry timeEntry) {
        long id = timeEntryMap.size() + 1;

        timeEntry.setId(id);
        timeEntryMap.put(id, timeEntry);

        return timeEntry;
    }

    public TimeEntry find(long id) {
        return timeEntryMap.get(id);
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntryMap.values());
    }

    public TimeEntry update(long id, TimeEntry desiredTimeEntry) {
        TimeEntry updatedTimeEntry = new TimeEntry(
                id,
                desiredTimeEntry.getProjectId(),
                desiredTimeEntry.getUserId(),
                desiredTimeEntry.getDate(),
                desiredTimeEntry.getHours()
        );

        timeEntryMap.put(id, updatedTimeEntry);

        return updatedTimeEntry;
    }

    public void delete(long id) {
        timeEntryMap.remove(id);
    }
}
