package project.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import project.voting.entity.Voter;
import java.util.Optional;

public interface VoterRepository extends JpaRepository<Voter, Integer> {
    Optional<Voter> findByAdmissionNumber(String admissionNumber);
    Optional<Voter> findByEmail(String email);

    @Modifying
    @Query("UPDATE Voter v SET v.hasVoted = false")
    void resetAllVoters();
}