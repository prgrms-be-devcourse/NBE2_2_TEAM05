package edu.example.dev_2_cc.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class MemberImage {

    private String filename;

}