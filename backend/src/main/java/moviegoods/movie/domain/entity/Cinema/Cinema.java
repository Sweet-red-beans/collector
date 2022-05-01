package moviegoods.movie.domain.entity.Cinema;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moviegoods.movie.domain.entity.Event.Event;
import moviegoods.movie.domain.entity.Post.Post;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@Entity(name="cinema")
@NoArgsConstructor
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cinema_id;

    @NotBlank
    @Column(length = 30)
    private String name;

    @Column(length = 30)
    private String area;

    @Column(length = 50)
    private String branch;

    @OneToOne(mappedBy = "cinema")
    private Post post;

    @OneToOne(mappedBy = "cinema")
    private Event event;



}
