package silkentrance.swearjar.api;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiResponse {
    private String errorMessage;
}
