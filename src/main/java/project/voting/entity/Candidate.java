package project.voting.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String party;
    private String school;
    private String position; // NEW: e.g., "School Rep", "Mens Rep", "Womens Rep"
    private int voteCount = 0;

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getParty() { return party; }
    public void setParty(String party) { this.party = party; }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public int getVoteCount() { return voteCount; }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }
}