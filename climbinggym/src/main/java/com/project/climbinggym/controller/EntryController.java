package com.project.climbinggym.controller;

import com.project.climbinggym.model.EntryType;
import com.project.climbinggym.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/entry")
@CrossOrigin(origins="*")
public class EntryController {

    @Autowired
    private EntryService entryService;


    // User actions
    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseEntry(
            @RequestParam String userId,
            @RequestParam String entryTypeId,
            @RequestParam(defaultValue = "false") boolean isReduced) {
        try {
            boolean purchased = entryService.purchaseEntryForUser(userId, entryTypeId, isReduced);

            if (purchased) {
                return new ResponseEntity<>("Entry purchased successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Failed to purchase entry", HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/use")
    public ResponseEntity<String> useEntry(
            @RequestParam String userId,
            @RequestParam String entryId,
            @RequestParam(defaultValue = "1") int amount ) {
        try {
            boolean used = entryService.useEntry(userId, entryId, amount);

            if (used) {
                return new ResponseEntity<>("Entry used successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to use entry", HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<EntryType>> getEntryTypes() {
        List<EntryType> entryTypes = entryService.getAllEntryTypes();
        return new ResponseEntity<>(entryTypes, HttpStatus.OK);
    }

    //może niekonieczne w kontrolerze?
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Integer>> getUsersEntryCount(@PathVariable String userId) {
        Map<String, Integer> userCounts = entryService.getUsersEntryCount(userId);
        return new ResponseEntity<>(userCounts, HttpStatus.OK);
    }


    // Admin actions
    @PostMapping
    public ResponseEntity<EntryType> createEntryType(@RequestBody EntryType entryType) {
        try{
            EntryType newType = entryService.createEntryType(entryType);
            return new ResponseEntity<>(newType, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntryType> updateEntryType(@PathVariable String id, @RequestBody EntryType entryType) {
        try{
            EntryType newEntry = entryService.updateEntryType(id, entryType);
            return new ResponseEntity<>(newEntry, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntryType(@PathVariable String id) {
        try{
            entryService.deleteEntryType(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }





}
