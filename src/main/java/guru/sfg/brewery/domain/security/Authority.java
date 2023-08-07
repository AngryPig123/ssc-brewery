package guru.sfg.brewery.domain.security;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "authority")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {


    @Id
    @Column(name = "AUTHORITY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String role;

    @ManyToMany(mappedBy = "authorities")
    private Set<User> users;

}
