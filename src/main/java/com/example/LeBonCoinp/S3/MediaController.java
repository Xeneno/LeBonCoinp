package com.example.LeBonCoinp.S3;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class MediaController {

    private final S3Service s3Service;

    @PostMapping("/image")
    public ResponseEntity<Map<String, Object>> generatePresignedUrl(
            @RequestParam String fileName,  // we get this from frontend url
            @RequestParam(defaultValue = "15") int expiryMinutes,
            @RequestParam long fileSize,
            @AuthenticationPrincipal Principal user  // to make sure only authenticated users can upload
    ) {

        if(fileSize > 5 * 1024 * 1024) { 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File size exceeds the maximum limit of 5MB");
        }

        if (!isValidImageExtension(fileName)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only image files allowed (.jpg, .jpeg, .png");
    }

        // Generate secure fileKey on backend
        String userId = user.getName(); 
        String contentType = getContentType(fileName);  
        String safeFileName = sanitizeFileName(fileName);
        String fileKey = String.format("items/%s/%d_%s", userId, System.currentTimeMillis(), safeFileName);

        // TODO: Make sure the image is not orphaned later if the item creation is not completed
        
        Map<String, Object> response = s3Service.generatePresignedUploadUrl(fileKey, expiryMinutes, contentType);
        return ResponseEntity.ok(response);
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
    "jpg", "jpeg", "png"
    );


    
    private boolean isValidImageExtension(String fileName) {
        String lower = fileName.toLowerCase();
        return ALLOWED_EXTENSIONS.stream() // ["jpg", "jpeg", "png"]
                .anyMatch(ext -> lower.endsWith("." + ext)); // anymatch Returns whether any elements of this stream match the provided predicate.
    }
    private String getContentType(String fileName) {
    String lower = fileName.toLowerCase();
    if (lower.endsWith(".png")) return "image/png";
    if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported file type");
}


}
