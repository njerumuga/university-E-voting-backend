package project.voting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "election_settings")
public class ElectionSettings {
    @Id
    private Integer id = 1; // Always 1 to keep a single control row

    private String currentPhase;

    // Standard Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCurrentPhase() { return currentPhase; }
    public void setCurrentPhase(String currentPhase) { this.currentPhase = currentPhase; }
}