package org.chumak.recommender.data.domain.review;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.chumak.recommender.data.common.model.BaseEntity;
import org.chumak.recommender.data.domain.user.User;
import org.chumak.recommender.data.domain.work.Work;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Table(name = "reviews")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Review extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "work_id")
    private Work work;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer rating;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;
}