package project.voting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.voting.entity.ElectionSettings;
import project.voting.repository.ElectionSettingsRepository;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/voters/settings")
@CrossOrigin(origins = "*")
public class SettingsController {

    @Autowired
    private ElectionSettingsRepository repository;

    @GetMapping("/phase")
    public ResponseEntity<Map<String, String>> getPhase() {
        String phase = repository.findById(1)
                .map(ElectionSettings::getCurrentPhase)
                .orElse("registration");

        Map<String, String> response = new HashMap<>();
        // Use .put() NOT .setData()
        response.put("phase", phase);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/phase")
    public ResponseEntity<Map<String, String>> updatePhase(@RequestBody Map<String, String> payload) {
        String newPhase = payload.get("phase");

        // Find existing settings or create a new one if it doesn't exist
        ElectionSettings settings = repository.findById(1).orElse(new ElectionSettings());
        settings.setId(1); // Force ID 1 so we always update the same row
        settings.setCurrentPhase(newPhase);

        repository.save(settings);

        Map<String, String> response = new HashMap<>();
        response.put("phase", settings.getCurrentPhase());
        return ResponseEntity.ok(response);
    }
}