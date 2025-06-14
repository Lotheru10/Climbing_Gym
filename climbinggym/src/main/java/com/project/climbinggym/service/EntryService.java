package com.project.climbinggym.service;

import com.project.climbinggym.model.nested.user.Entry;

import com.project.climbinggym.model.EntryType;
import com.project.climbinggym.model.User;
import com.project.climbinggym.repository.EntryTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EntryService {

    @Autowired
    private UserService userService;

    @Autowired
    private EntryTypeRepository entryTypeRepository;

    // Create
    public EntryType createEntryType(EntryType entryType) {
        return entryTypeRepository.save(entryType);
    }

    // Read
    public Optional<EntryType> getEntryTypeByType(String entryType) {
        return entryTypeRepository.findByEntryType(entryType);
    }

    public List<EntryType> getAllEntryTypes() {
        return entryTypeRepository.findAll();
    }

    // Delete
    public void deleteEntryType(String id) {
        if (entryTypeRepository.existsById(id)) {
            entryTypeRepository.deleteById(id);
        } else {
            throw new RuntimeException("Entry type not found with id: " + id);
        }
    }

    // Update
    public EntryType updateEntryType(String id, EntryType updatedEntryType) {
        return entryTypeRepository.findById(id)
                .map(entryType -> {
                    entryType.setEntryType(updatedEntryType.getEntryType());
                    entryType.setName(updatedEntryType.getName());
                    entryType.setPrices(updatedEntryType.getPrices());
                    entryType.setDayLimit(updatedEntryType.getDayLimit());
                    entryType.setUses(updatedEntryType.getUses());
                    return entryTypeRepository.save(entryType);
                })
                .orElseThrow(() -> new RuntimeException("Entry type not found with id: " + id));
    }

    // User Actions
    @Transactional
    public boolean purchaseEntryForUser(String userId, String entryTypeId, boolean isReduced) {
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            EntryType entryType = entryTypeRepository.findById(entryTypeId)
                    .orElseThrow(() -> new RuntimeException("Entry type not found with id: " + entryTypeId));

            Entry entry = new Entry();
            if (isReduced) {
                entry.setType("Reduced");
            } else {
                entry.setType("Regular");
            }
            entry.setPrices(entryType.getPrices());
            entry.setDeadline(LocalDate.now().plusDays(entryType.getUses()));
            entry.setAmount(entryType.getUses());

            user.getEntries().add(entry);
            userService.updateUser(userId, user);
            return true;

        } catch (Exception e) {
            throw new RuntimeException("Failed to purchase entry: " + e.getMessage(), e);
        }
    }

    @Transactional
    public boolean useEntry(String userId, String entryId, int amount) {
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            Entry entry = user.getEntries().stream()
                    .filter(e -> e.getEntryId().equals(entryId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Entry not found"));

            if (entry.getAmount() < amount) {
                throw new RuntimeException("Entry does not have enough uses");
            }

            entry.setAmount(entry.getAmount() - amount);
            userService.updateUser(userId, user);
            return true;

        } catch (Exception e) {
            throw new RuntimeException("Failed to use entry: " + e.getMessage(), e);
        }
    }


    // Aktywne wejściówki
    public List<Entry> getUserActiveEntries(String userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return user.getEntries().stream()
                .filter(entry -> entry.getAmount() > 0)
                .filter(entry -> entry.getDeadline().isAfter(LocalDate.now()))
                .toList();
    }

    // Aktywne wejściówki w dzień rezerwacji
    public List<Entry> getUserActiveEntriesForDate(String userId, LocalDate date) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return user.getEntries().stream()
                .filter(entry -> entry.getAmount() > 0)
                .filter(entry -> entry.getDeadline().isAfter(date))
                .toList();
    }

    // Liczba wejść
    public Map<String, Integer> getUsersEntryCount(String userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Map<String, Integer> entryCounts = new HashMap<>();
        entryCounts.put("regular", 0);
        entryCounts.put("reduced", 0);

        getUserActiveEntries(userId).forEach(entry -> {
            String type = entry.getType().toLowerCase();
            int amount = entry.getAmount();

            entryCounts.put(type, entryCounts.get(type) + amount);
        });

        return entryCounts;
    }

    // Liczba wejść na dany dzień
    public Map<String, Integer> getUsersEntryCountForDate(String userId,LocalDate date) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Map<String, Integer> entryCounts = new HashMap<>();
        entryCounts.put("regular", 0);
        entryCounts.put("reduced", 0);

        getUserActiveEntriesForDate(userId, date).forEach(entry -> {
            String type = entry.getType().toLowerCase();
            int amount = entry.getAmount();

            entryCounts.put(type, entryCounts.get(type) + amount);
        });

        return entryCounts;
    }


}
