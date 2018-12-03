package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private final CounterService counter;
    private final GaugeService gauge;
    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository ,CounterService counter,
                               GaugeService gauge) {
        this.timeEntryRepository=timeEntryRepository;
        this.counter = counter;
        this.gauge = gauge;
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry){

        counter.increment("TimeEntry.created");
        gauge.submit("timeEntries.count", timeEntryRepository.list().size());
        ResponseEntity<TimeEntry> response = new ResponseEntity<TimeEntry>(timeEntryRepository.create(timeEntry), HttpStatus.CREATED);
        return response;

    }

    @GetMapping("{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long timeEntryId){
        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        ResponseEntity<TimeEntry> response = null;
        if(timeEntry != null) {
            counter.increment("TimeEntry.read");
            response = new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
        } else {
            response = new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }
        return response;

    }


    @GetMapping
    public ResponseEntity<List<TimeEntry>> list(){
        counter.increment("TimeEntry.listed");
        ResponseEntity<List<TimeEntry>> response = new ResponseEntity<List<TimeEntry>>(timeEntryRepository.list(), HttpStatus.OK);
        return response;

    }

    @PutMapping("{timeEntryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable Long timeEntryId, @RequestBody TimeEntry timeEntry){
        ResponseEntity<TimeEntry> response = null;
        System.out.println("id ==================================== "+timeEntryId);
        TimeEntry updated = timeEntryRepository.update(timeEntryId, timeEntry);
        if(updated == null) {
            counter.increment("TimeEntry.updated");
            response = new ResponseEntity<TimeEntry>(updated, HttpStatus.NOT_FOUND);
        }
        else {
            response = new ResponseEntity<TimeEntry>(updated, HttpStatus.OK);
        }

        return response;

    }


    @DeleteMapping("{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long timeEntryId){
        ResponseEntity<TimeEntry> response = null;
        timeEntryRepository.delete(timeEntryId);
        counter.increment("TimeEntry.deleted");
        gauge.submit("timeEntries.count", timeEntryRepository.list().size());
        response = new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
        return response;

    }
}
