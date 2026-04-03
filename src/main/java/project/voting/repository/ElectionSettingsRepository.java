package project.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.voting.entity.ElectionSettings;

@Repository
public interface ElectionSettingsRepository extends JpaRepository<ElectionSettings, Integer> {
    // This allows the controller to use .findById(1) and .save()
}