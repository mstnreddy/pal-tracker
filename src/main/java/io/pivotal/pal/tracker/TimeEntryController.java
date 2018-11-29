package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository=timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry){
        ResponseEntity<TimeEntry> response = new ResponseEntity<TimeEntry>(timeEntryRepository.create(timeEntry), HttpStatus.CREATED);
        return response;

    }

    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long timeEntryId){
        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        ResponseEntity<TimeEntry> response = null;
        if(timeEntry != null) {
            response = new ResponseEntity<TimeEntry>(timeEntry, HttpStatus.OK);
        } else {
            response = new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }
        return response;

    }


    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list(){
        ResponseEntity<List<TimeEntry>> response = new ResponseEntity<List<TimeEntry>>(timeEntryRepository.list(), HttpStatus.OK);
        return response;

    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> update(@PathVariable Long timeEntryId, @RequestBody TimeEntry timeEntry){
        ResponseEntity<TimeEntry> response = null;
        System.out.println("id ==================================== "+timeEntryId);
        TimeEntry updated = timeEntryRepository.update(timeEntryId, timeEntry);
        if(updated == null) {
            response = new ResponseEntity<TimeEntry>(updated, HttpStatus.NOT_FOUND);
        }
        else {
            response = new ResponseEntity<TimeEntry>(updated, HttpStatus.OK);
        }

        return response;

    }


    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long timeEntryId){
        ResponseEntity<TimeEntry> response = null;
        timeEntryRepository.delete(timeEntryId);
        response = new ResponseEntity<TimeEntry>(HttpStatus.NO_CONTENT);
        return response;

    }
}
