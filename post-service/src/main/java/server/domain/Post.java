package server.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;
@Entity
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PUBLIC)
public class Post extends BaseTimeEntity{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "post_id")
        private Long id;

        @Column(nullable = false)
        private Long writerId;

        @Column(length = 25, nullable = false)
        private String title;

        @Column(length = 100, nullable = false)
        private String content;

        public Post updatePost(String content, String title) {
                return this.toBuilder()
                        .content(content)
                        .title(title)
                        .build();
        }

}
