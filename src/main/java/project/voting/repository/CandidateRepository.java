package project.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import project.voting.entity.Candidate;
import org.springframework.transaction.annotation.Transactional;

public interface CandidateRepository extends JpaRepository<Candidate, Integer> {

    // ✅ Reset all votes (used during election reset)
    @Modifying
    @Transactional
    @Query("UPDATE Candidate c SET c.voteCount = 0")
    void resetAllCounts();
}