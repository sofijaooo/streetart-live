package com.streetarts.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/artist-account")
@CrossOrigin(origins = "*")
public class ArtistAccountController {

    private final ArtistRepository artistRepository;
    private final ArtistMediaRepository mediaRepository;
    private final CloudinaryService cloudinaryService;

    public ArtistAccountController(
            ArtistRepository artistRepository,
            ArtistMediaRepository mediaRepository,
            CloudinaryService cloudinaryService
    ) {
        this.artistRepository = artistRepository;
        this.mediaRepository = mediaRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getArtist(@PathVariable("userId") Integer userId) {
        Artists artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Профіль митця не знайдено"));

        return ResponseEntity.ok(artist);
    }

    @PostMapping("/{userId}/avatar")
    public ResponseEntity<?> uploadAvatar(
            @PathVariable("userId") Integer userId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            Artists artist = artistRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Профіль митця не знайдено"));

            if (file.isEmpty()) {
                throw new RuntimeException("Оберіть файл аватарки");
            }

            if (!file.getContentType().startsWith("image/")) {
                throw new RuntimeException("Аватарка має бути зображенням");
            }

            if (artist.getAvatarPublicId() != null && !artist.getAvatarPublicId().isBlank()) {
                cloudinaryService.delete(artist.getAvatarPublicId(), "image");
            }

            Map upload = cloudinaryService.upload(file, "streetart/avatars", "image");

            artist.setAvatar_url((String) upload.get("secure_url"));
            artist.setAvatarPublicId((String) upload.get("public_id"));

            Artists saved = artistRepository.save(artist);

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/media")
    public ResponseEntity<?> getMedia(@PathVariable("userId") Integer userId) {
        Artists artist = artistRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Профіль митця не знайдено"));

        List<ArtistMedia> media = mediaRepository.findByArtistIdOrderByCreatedAtDesc(artist.getId());

        return ResponseEntity.ok(media);
    }

    @PostMapping("/{userId}/media")
    public ResponseEntity<?> uploadMedia(
            @PathVariable("userId") Integer userId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            Artists artist = artistRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Профіль митця не знайдено"));

            if (file.isEmpty()) {
                throw new RuntimeException("Оберіть файл");
            }

            String contentType = file.getContentType();

            if (contentType == null ||
                    (!contentType.startsWith("image/") && !contentType.startsWith("video/"))) {
                throw new RuntimeException("Можна завантажувати лише фото або відео");
            }

            String mediaType = contentType.startsWith("video/") ? "video" : "image";

            Map upload = cloudinaryService.upload(
                    file,
                    "streetart/media",
                    mediaType
            );

            ArtistMedia media = new ArtistMedia();
            media.setArtistId(artist.getId());
            media.setMediaUrl((String) upload.get("secure_url"));
            media.setPublicId((String) upload.get("public_id"));
            media.setMediaType(mediaType);

            ArtistMedia saved = mediaRepository.save(media);

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/media/{mediaId}")
    public ResponseEntity<?> deleteMedia(@PathVariable("mediaId") Long mediaId) {
        try {
            ArtistMedia media = mediaRepository.findById(mediaId)
                    .orElseThrow(() -> new RuntimeException("Медіафайл не знайдено"));

            cloudinaryService.delete(media.getPublicId(), media.getMediaType());
            mediaRepository.delete(media);

            return ResponseEntity.ok(Map.of("message", "Медіафайл видалено"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}