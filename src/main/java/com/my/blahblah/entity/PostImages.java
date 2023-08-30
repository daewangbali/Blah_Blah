package com.my.blahblah.entity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "post")
public class PostImages implements Comparable<PostImages> {
	
    @Id
    private String uuid;

    private String fileName;

    private int ord;

    @ManyToOne
    @JoinColumn(name = "post_no")
    private Posts post;


    @Override
    public int compareTo(PostImages other) {
        return this.ord - other.ord;
    }

    public void changeBoard(Posts post){
        this.post = post;
    }

}
