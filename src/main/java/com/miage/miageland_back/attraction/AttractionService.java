package com.miage.miageland_back.attraction;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttractionService {

    private final AttractionRepository attractionRepository;

    public Attraction createAttraction(Attraction newAttraction) {
        if (this.attractionRepository.existsByName(newAttraction.getName())) {
            throw new EntityExistsException("Attraction already exists");
        } else {
            newAttraction.setOpen(false);
            this.attractionRepository.save(newAttraction);
            return newAttraction;
        }
    }

    @Transactional
    public void deleteAttraction(Long attractionId) {
        if (this.attractionRepository.existsById(attractionId)) {
            this.attractionRepository.deleteById(attractionId);
        } else {
            throw new EntityNotFoundException("Attraction does not exist");
        }
    }

    public AttractionDTO changeAttractionStatus(Long attractionId) {
        if (this.attractionRepository.existsById(attractionId)) {
            Attraction attractionToUpdate = this.attractionRepository.findById(attractionId).get();
            attractionToUpdate.setOpen(!attractionToUpdate.isOpen());
            this.attractionRepository.save(attractionToUpdate);

            AttractionDTO attractionDTO = new AttractionDTO();
            attractionDTO.setName(attractionToUpdate.getName());
            attractionDTO.setOpen(attractionToUpdate.isOpen());
            return attractionDTO;
        } else {
            throw new EntityNotFoundException("Attraction does not exist");
        }
    }

    public List<Attraction> getOpenedAttractions() {
        return this.attractionRepository.findByIsOpenTrue();
    }

    public Attraction getAttraction(Long attractionId) {
        return this.attractionRepository.findById(attractionId)
                .orElseThrow(() -> new EntityNotFoundException("Attraction does not exist"));
    }

    public List<Attraction> getAttractions() {
        return this.attractionRepository.findAll();
    }
}