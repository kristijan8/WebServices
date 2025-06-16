package com.mcnz.spring.soaprest.Models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "creator",
            cascade = CascadeType.ALL,
            orphanRemoval = true)

    private List<Club> clubsCreated = new ArrayList<>();

    // getters/setters
    public List<Club> getClubsCreated() {
        return clubsCreated;
    }
    public void setClubsCreated(List<Club> clubsCreated) {
        this.clubsCreated = clubsCreated;
    }
    // add helper
    public void addClub(Club club) {
        clubsCreated.add(club);
        club.setCreator(this);
    }
    public void removeClub(Club club) {
        clubsCreated.remove(club);
        club.setCreator(null);
    }


    @Column(name = "last_jti")
    private String lastJti;


}
