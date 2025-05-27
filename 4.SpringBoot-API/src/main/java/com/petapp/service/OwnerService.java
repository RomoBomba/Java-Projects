package com.petapp.service;

import com.petapp.dto.OwnerDto;
import com.petapp.entities.Owner;
import com.petapp.mapper.OwnerMapper;
import com.petapp.repository.OwnerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    public OwnerService(OwnerRepository ownerRepository, OwnerMapper ownerMapper) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
    }

    public List<OwnerDto> getAllOwners() {
        return ownerRepository.findAll()
                .stream()
                .map(ownerMapper::toDto)
                .collect(Collectors.toList());
    }

    public OwnerDto getOwnerById(Long id) {
        Owner owner = ownerRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + id));
        return ownerMapper.toDto(owner);
    }

    public OwnerDto createOwner(OwnerDto ownerDto) {
        Owner savedOwner = ownerRepository.save(ownerMapper.toEntity(ownerDto));
        return ownerMapper.toDto(savedOwner);
    }

    public OwnerDto updateOwner(Long id, OwnerDto ownerDto) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + id));

        owner.setName(ownerDto.getName());
        owner.setBirthDate(ownerDto.getBirthDate());
        Owner updated = ownerRepository.save(owner);

        return ownerMapper.toDto(updated);
    }

    public void deleteOwner(Long id) {
        if (!ownerRepository.existsById(id)) {
            throw new EntityNotFoundException("Owner not found with id: " + id);
        } // todo: ExpectionHandler
        ownerRepository.deleteById(id);
    }
}