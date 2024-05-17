package com.todochat.todochat.models;


import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    
    @OneToMany(mappedBy = "project",fetch = FetchType.EAGER)
    private List<Developer> developers;

    @OneToMany(mappedBy = "project",fetch = FetchType.EAGER)
    private List<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "managerId")
    private Manager manager;
}
