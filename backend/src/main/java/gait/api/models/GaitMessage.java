package gait.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class GaitMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String message;
}
