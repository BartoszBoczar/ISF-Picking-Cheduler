import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class Picker {
    private String pickerId;
    private LocalTime availabilityTime;

    public void increaseAvailabilityTime(Duration duration) {
        availabilityTime = availabilityTime.plusMinutes(duration.toMinutes());
    }

    public void decreaseAvailabilityTime(Duration duration) {
        availabilityTime = availabilityTime.minusMinutes(duration.toMinutes());
    }

}
