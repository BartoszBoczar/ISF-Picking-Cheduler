import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Duration;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
public class Order {
    private String orderId;
    private double orderValue;
    private Duration pickingTime;
    private LocalTime completeBy;
    // Final time at which the order must be picked or won't be completed on time
    private LocalTime finalTime;
}
