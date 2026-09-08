package org.mserz_o.springcourse.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="Players")
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @NonNull
    private String name;

}
