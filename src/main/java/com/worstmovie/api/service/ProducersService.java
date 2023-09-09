package com.worstmovie.api.service;
import com.worstmovie.api.cache.CacheStoreProducers;
import com.worstmovie.api.dto.response.ProducerResponseDTO;
import com.worstmovie.api.dto.request.ProducerRequestDTO;
import com.worstmovie.api.dto.response.WorstMovieResponseDTO;
import com.worstmovie.api.model.Producer;
import com.worstmovie.api.repository.ProducersRepository;
import com.worstmovie.api.utils.CSVMovieListUtils;
import com.worstmovie.api.utils.LinksUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProducersService {

    @Inject
    ProducersRepository producersRepository;

    @Inject
    CacheStoreProducers cacheStoreProducers;

    public Producer saveProducer(String name) {
        return producersRepository.save(Producer
                .builder()
                .name(name)
                .build());
    }

    public void deleteProducers(Long id) {
        Producer.deleteById(id);
    }

    public void updateProducers(Long id, ProducerRequestDTO producersRequestDTO) {
        Producer producer = Producer.findById(id);
        producer.setName(producersRequestDTO.getName());
        producersRepository.save(producer);
    }

    public List<Producer> saveAll(List<Producer> producers) {
        List<Producer> producerSaveds = new ArrayList<>();
        producers.forEach(producer -> {
            Producer producerCache = cacheStoreProducers.storeProducers().get(producer.getName());
            if (Objects.nonNull(producerCache)) {
                producerSaveds.add(producerCache);
            } else {
                producerCache = producersRepository.findByName(producer.getName()).orElse(null);
                if (Objects.isNull(producerCache)) {
                    producerCache = producersRepository.save(producer);
                }
                cacheStoreProducers.storeProducers().add(producerCache.getName(), producerCache);
                producerSaveds.add(producerCache);
            }
        });
        return producerSaveds;
    }

    public List<ProducerResponseDTO> toProducersResponseDTO(List<Producer> producers, String pathRequest) {
        List<ProducerResponseDTO> producersDTOS = new ArrayList<>();
        producers.forEach(producer -> producersDTOS.add(ProducerResponseDTO.builder()
                .id(producer.getId())
                .name(producer.getName())
                .links(LinksUtils.generateLinks(pathRequest, producer.getId()))
                .worstMovies(returnWorstMovieProducer(producer))
                .build()));
        return producersDTOS;
    }

    private List<WorstMovieResponseDTO> returnWorstMovieProducer(Producer producer) {
        List<WorstMovieResponseDTO> worstMovies = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(producer.getWorstMovieProducers())) {
            producer.getWorstMovieProducers().forEach(worstMovieProducer -> worstMovies.add(WorstMovieResponseDTO
                    .builder()
                    .id(worstMovieProducer.getId())
                    .year(worstMovieProducer.getWorstMovie().getYear())
                    .title(worstMovieProducer.getWorstMovie().getTitle())
                    .winner(worstMovieProducer.getWorstMovie().isWinner())
                    .build()));
        }
        return worstMovies;
    }

    public ProducerResponseDTO toProducerResponseDTO(Producer producer, String path) {
        return ProducerResponseDTO
                .builder()
                .id(producer.getId())
                .name(producer.getName())
                .worstMovies(returnWorstMovieProducer(producer))
                .links(LinksUtils.generateLinks(path, null))
                .build();
    }

    public List<Producer> returnProducersFromCSVRecord(CSVRecord producerRecord) {
        return CSVMovieListUtils.splitAndCleanRecords(producerRecord, CSVMovieListUtils.HEADER_PRODUCERS).stream()
                .map(this::buildProducer)
                .collect(Collectors.toList());
    }

    private Producer buildProducer(String nameProducer) {
        return Producer.builder()
                .name((nameProducer))
                .build();
    }
}
