package org.sid.authjwt.security.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    // comment json ignore, il va l'ignore au moment de la serialisation
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @ManyToMany(fetch = FetchType.EAGER) // par default fetch = FetchType.LAZY
    private Collection<AppRole> appRoles = new ArrayList<>();
    // Quand vous utilisez EAGER il est preferable d'initialiser la Collection;
}
