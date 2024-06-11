package com.esquinaPet.veterinariabackend.domain.models;


import com.esquinaPet.veterinariabackend.domain.utils.enums.Role;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Pattern(regexp = "^[A-Za-záéíóúüñÁÉÍÓÚÜÑ]+$")
    @Size(min = 3, max = 50)
    private String name;
    @NotNull
    @Pattern(regexp = "^[A-Za-záéíóúüñÁÉÍÓÚÜÑ]+$")
    @Column(name = "last_name")
    @Size(min = 3, max = 50)
    private String lastName;
    @NotNull
    @Email
    @Column(unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$")
    private String email;
    @NotNull
    @Column(unique = true)
    @Pattern(regexp = "^\\+569\\d{8}$")
    @Size(min = 12, max = 12)
//    @Pattern(regexp = "^[0-9]{9}$")
    private String phone;
    @NotNull
    @Pattern(regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}-[\\dk]$")
    @Column(unique = true)
    @Size(min = 12, max = 12)
    private String rut;
    @NotNull
    @Size(min = 6, max = 2000)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9]).+$")
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Appointment> appointments;
    @NotNull
    @Column(name = "is_active")
    private Boolean isActive;

    // metodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return null;
        }
        if (role.getPermissions() == null) {
            return null;
        }

        List<SimpleGrantedAuthority> authorities = role.getPermissions().stream().map(each -> each.name())
                .map(each -> new SimpleGrantedAuthority(each))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}