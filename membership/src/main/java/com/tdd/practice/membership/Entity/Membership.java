package com.tdd.practice.membership.Entity;

import com.tdd.practice.membership.Enums.MembershipType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long  id;

   @Enumerated(EnumType.STRING)
   @Setter
   private MembershipType membershipType;

    @Column(nullable = false, length = 20)
    @Setter
    private String userId;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Setter
    private Integer point;

    @CreationTimestamp
    @Column(nullable = false, length = 20, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(length = 20)
    private LocalDateTime updatedAt;


}
