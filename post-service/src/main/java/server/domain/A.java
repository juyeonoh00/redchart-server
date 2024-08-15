package server.domain;

import jakarta.persistence.*;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PUBLIC)
public class A  {
    @Id
    private String dtoId;


}