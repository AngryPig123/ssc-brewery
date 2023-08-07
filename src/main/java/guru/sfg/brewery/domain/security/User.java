package guru.sfg.brewery.domain.security;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * The type User.
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @Singular   //  ToDO 조사 필요
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "AUTHORITY_ID")}
    )
    private Set<Authority> authorities;

    @Builder.Default
    private boolean accountNonExpired = true;

    @Builder.Default
    private boolean accountNonLocked = true;

    @Builder.Default
    private boolean credentialsNonExpired = true;

    @Builder.Default
    private boolean enabled = true;

}
