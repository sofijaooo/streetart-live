package com.streetarts.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArtistMediaRepository extends JpaRepository<ArtistMedia, Long> {
    List<ArtistMedia> findByArtistIdOrderByCreatedAtDesc(Long artistId);
}