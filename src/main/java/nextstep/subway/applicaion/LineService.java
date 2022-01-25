package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.DuplicateLineException;
import nextstep.subway.exception.LineNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository,
                       StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new DuplicateLineException(request.getName());
        }

        Station upStation = stationService.findStationsById(request.getUpStationId());
        Station downStation = stationService.findStationsById(request.getDownStationId());

        Line line = lineRepository.save(
                Line.of(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    @Transactional
    public ShowLineResponse addSection(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationsById(request.getUpStationId());
        Station downStation = stationService.findStationsById(request.getDownStationId());

        line.addSection(upStation, downStation, request.getDistance());

        return createShowLineResponse(line);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station deleteStation = stationService.findStationsById(stationId);
        line.deleteStation(deleteStation);
    }

    public List<ShowLineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::createShowLineResponse2)
                .collect(toList());
    }

    public ShowLineResponse findLine(Long id) {
        Line line = findLineById(id);

        return createShowLineResponse(line);
    }

    @Transactional
    public void updateLine(Long id, UpdateLineRequest request) {
        Line line = findLineById(id);
        line.updateInfo(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException());
    }

    private ShowLineResponse createShowLineResponse2(Line line) {
        return ShowLineResponse.of(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate(),
                line.getAllStations()
        );
    }

    private ShowLineResponse createShowLineResponse(Line line) {
        return ShowLineResponse.of(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

}
