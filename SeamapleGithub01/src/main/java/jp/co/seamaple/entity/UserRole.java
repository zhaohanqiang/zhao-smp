package jp.co.seamaple.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "t_user_role")
public class UserRole implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "role_id")
    private String roleId;

    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ToString.Exclude // stack over flow error 防止
    private User user;
}
