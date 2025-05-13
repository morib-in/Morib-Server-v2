package org.morib.server.domain.user.infra;

import jakarta.persistence.*;
import lombok.*;
import org.morib.server.global.common.BaseTimeEntity;

@Entity
@Getter
@Builder(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WaitingUserWindow extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waiting_user_window_id")
    private Long id;
    private String email;

    public static WaitingUserWindow create(String email) {
        return WaitingUserWindow.builder()
                .email(email)
                .build();
    }
}
