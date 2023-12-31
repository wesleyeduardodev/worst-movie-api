package com.worstmovie.api.service;
import com.worstmovie.api.dto.response.StudioResponseDTO;
import com.worstmovie.api.dto.request.StudioRequestDTO;
import com.worstmovie.api.dto.response.WorstMovieResponseDTO;
import com.worstmovie.api.model.Studio;
import com.worstmovie.api.repository.StudiosRepository;
import com.worstmovie.api.utils.CSVMovieListUtils;
import com.worstmovie.api.utils.LinksUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class StudiosService {

    @Inject
    StudiosRepository studiosRepository;

    public List<Studio> findAllStudios() {
        return Studio.listAll();
    }

    public Studio saveStudio(Studio studio) {
        return studiosRepository.save(studio);
    }

    public void deleteStudio(Long id) {
        Studio.deleteById(id);
    }

    public void updateStudio(Long id, StudioRequestDTO studioRequestDTO) {
        Studio studio = Studio.findById(id);
        studio.setName(studioRequestDTO.getName());
        studiosRepository.save(studio);
    }

    public StudioResponseDTO toStudioResponseDTO(Studio studio, String path) {
        return StudioResponseDTO
                .builder()
                .id(studio.getId())
                .name(studio.getName())
                .worstMovies(returnWorstMovieStudio(studio))
                .links(LinksUtils.generateLinks(path, null))
                .build();
    }

    public List<StudioResponseDTO> toStudiosResponseDTO(List<Studio> studios, String path) {
        List<StudioResponseDTO> studioResponseDTOS = new ArrayList<>();
        studios.forEach(studio -> studioResponseDTOS.add(StudioResponseDTO.builder()
                .id(studio.getId())
                .name(studio.getName())
                .links(LinksUtils.generateLinks(path, studio.getId()))
                .worstMovies(returnWorstMovieStudio(studio))
                .build()));
        return studioResponseDTOS;
    }

    private List<WorstMovieResponseDTO> returnWorstMovieStudio(Studio studio) {
        List<WorstMovieResponseDTO> worstMovies = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(studio.getWorstMovieStudios())) {
            studio.getWorstMovieStudios().forEach(worstMovieStudio -> worstMovies.add(WorstMovieResponseDTO
                    .builder()
                    .id(worstMovieStudio.getId())
                    .year(worstMovieStudio.getWorstMovie().getYear())
                    .title(worstMovieStudio.getWorstMovie().getTitle())
                    .winner(worstMovieStudio.getWorstMovie().isWinner())
                    .build()));
        }
        return worstMovies;
    }

    public List<Studio> returnStudiosFromCSVRecord(CSVRecord studioRecord) {
        return CSVMovieListUtils.splitAndCleanRecords(studioRecord, CSVMovieListUtils.HEADER_STUDIOS).stream()
                .map(this::buildStudios)
                .collect(Collectors.toList());
    }

    private Studio buildStudios(String record) {
        return Studio.builder()
                .name((record))
                .build();
    }
}
