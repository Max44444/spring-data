package com.bsa.springdata.office;

import com.bsa.springdata.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "offices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Office {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column
    private String city;

    @Column
    private String address;

    @OneToMany(mappedBy = "office", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;

}
