package project.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.voting.entity.Voter;
import java.util.Optional;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Integer> {
    // This fixed the error in your Controller!
    Optional<Voter> findByAdmissionNumber(String admissionNumber);

    // You can keep this if you still need to look up by email elsewhere
    Optional<Voter> findByEmail(String email);
}