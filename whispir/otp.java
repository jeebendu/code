import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "OTP")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "SESSION")
    private String session;

    @Column(name = "OTP")
    private String otp;

    @Column(name = "EXPIRY_TIME")
    private LocalDateTime expiryTime;

    @Column(name = "CREATED_TIME")
    private LocalDateTime createdTime;

    @Column(name = "USED_TIME")
    private LocalDateTime usedTime;

    @Column(name = "USED")
    private boolean used;

    @Column(name = "ATTEMPTS")
    private int attempts;

    // Getters and setters
    // ...
}