package io.pivotal.pal.tracker;

import ch.qos.logback.core.net.SyslogOutputStream;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    public Map<Long, TimeEntry> map = new HashMap<Long, TimeEntry>();

    @Override
    public TimeEntry create(TimeEntry timeentry) {
        System.out.println("map in create = "+map);
        timeentry.setId(map.size() + 1L);
        map.put(timeentry.getId(), timeentry);
        return timeentry;
    }

    @Override
    public TimeEntry find(long id) {
        System.out.println("map in find = "+map);
        return map.get(id);
    }

    @Override
    public void delete(long id) {
        System.out.println("map in delete = "+map);
        map.remove(id);
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeentry) {
        System.out.println("map in update = "+map);
        map.put(id, timeentry);
        timeentry.setId(id);
        return timeentry;
    }

    @Override
    public List<TimeEntry> list() {
        System.out.println("map in list = "+map);
        return new ArrayList<TimeEntry>(map.values());
    }
}
