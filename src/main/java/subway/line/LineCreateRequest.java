package subway.line;

import lombok.Getter;
import subway.station.Station;

@Getter
public class LineCreateRequest {
    private String name;
    private String color;
    private int distance;
    private Long upstationId;
    private Long downstationId;

    public static Line toEntity(LineCreateRequest request) {
        return Line.builder()
                .name(request.getName())
                .color(request.getColor())
                .distance(request.getDistance())
                .build();
    }
}