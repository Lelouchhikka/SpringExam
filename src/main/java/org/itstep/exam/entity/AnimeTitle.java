package org.itstep.exam.entity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.List;

public class AnimeTitle extends BaseEntity {
    @Column(name = "email", unique = true)
    private String Title;

    private String Description;

    private Integer NumberOfEpisodes;

    private Integer Year;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> genres;


}
