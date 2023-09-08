package com.worstmovie.api.service;
import com.worstmovie.api.dto.reesponse.StudioResponseDTO;
import com.worstmovie.api.dto.request.StudioRequestDTO;
import com.worstmovie.api.model.Studio;
import com.worstmovie.api.repository.StudiosRepository;
import com.worstmovie.api.utils.CSVMovieListUtils;
import com.worstmovie.api.utils.LinksUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.csv.CSVRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class StudiosService {

    @Inject
    StudiosRepository studiosRepository;

    public List<Studio> findAllStudios() {
        return Studio.listAll();
    }

    public Studio saveStudio(String name) {
        return studiosRepository.save(Studio
                .builder()
                .name(name)
                .build());
    }

    public void deleteStudio(Long id) {
        Studio.deleteById(id);
    }

    public void updateStudio(Long id, StudioRequestDTO studioRequestDTO) {
        Studio studio = Studio.findById(id);
        studio.setName(studioRequestDTO.getName());
        Studio.persist(studioRequestDTO);
    }

    public Studio saveOrReturnStudio(String name) {
        Optional<Studio> studio = studiosRepository.findByName(name);
        return studio.orElseGet(() -> studiosRepository.save(Studio.builder().name(name).build()));
    }

    public StudioResponseDTO studioEntityToStudioResponse(Studio studio, String path) {
        return StudioResponseDTO
                .builder()
                .id(studio.getId())
                .name(studio.getName())
                .links(LinksUtils.generateLinks(path, null))
                .build();
    }

    public List<StudioResponseDTO> studiosEntityToStudiosResponseDTO(List<Studio> studios, String path) {
        List<StudioResponseDTO> studioResponseDTOS = new ArrayList<>();
        studios.forEach(studio -> studioResponseDTOS.add(StudioResponseDTO.builder()
                .id(studio.getId())
                .name(studio.getName())
                .links(LinksUtils.generateLinks(path, studio.getId()))
                .build()));
        return studioResponseDTOS;
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
