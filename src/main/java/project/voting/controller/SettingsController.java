package project.voting.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.voting.entity.ElectionSettings;
import project.voting.repository.ElectionSettingsRepository;

import java.util.Map;

@RestController
@RequestMapping("/api/voters/settings")
@CrossOrigin(origins = "*")
public class SettingsController {

    @Autowired
    private ElectionSettingsRepository repository;

    @GetMapping("/phase")
    public ResponseEntity<String> getPhase() {
        return ResponseEntity.ok(repository.findById(1)
                .map(ElectionSettings::getCurrentPhase)
                .orElse("registration"));
    }

    @PostMapping("/phase")
    public ResponseEntity<String> updatePhase(@RequestBody Map<String, String> payload) {
        ElectionSettings settings = repository.findById(1).orElse(new ElectionSettings());
        settings.setCurrentPhase(payload.get("phase"));
        repository.save(settings);
        return ResponseEntity.ok(settings.getCurrentPhase());
    }
}
