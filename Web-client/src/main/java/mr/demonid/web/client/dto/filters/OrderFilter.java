package mr.demonid.web.client.dto.filters;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderFilter {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

