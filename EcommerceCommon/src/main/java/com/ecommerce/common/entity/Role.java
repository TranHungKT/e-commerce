package com.ecommerce.common.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 40, nullable = false, unique = true)
    private String name;

    @Column(length = 150, nullable = false)
    private String description;

    public Role(Integer id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
