package com.dripify.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.dripify.cloudinary.service.CloudinaryService;
import com.dripify.exception.CloudinaryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CloudinaryServiceUTest {

    @Mock
    private Cloudinary cloudinary;

    @InjectMocks
    private CloudinaryService cloudinaryService;

    @BeforeEach
    void setUp() {
        when(cloudinary.uploader()).thenReturn(mock(Uploader.class));
    }

    @Test
    void givenFileLargerThan1MB_whenUploadProfileImage_thenThrowsException() {
        Mockito.reset(cloudinary);

        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getSize()).thenReturn(1024L * 1024 * 500);

        // When && Then
        assertThrows(CloudinaryException.class, () -> cloudinaryService.uploadProfileImage(file));
        verifyNoInteractions(cloudinary);
    }

    @Test
    void givenIOException_whenUploadProfileImage_thenThrowsException() throws IOException {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getSize()).thenReturn(1024L);
        when(cloudinary.uploader().upload(any(), anyMap())).thenThrow(IOException.class);

        // When && Then
        assertThrows(CloudinaryException.class, () -> cloudinaryService.uploadProfileImage(file));
    }

    @Test
    void givenHappyPath_whenUploadProfileImage() throws IOException {
        //Given
        Map<String, String> uploadResult = Map.of("url", "www.image.com");
        MultipartFile file = mock(MultipartFile.class);
        when(file.getSize()).thenReturn(1024L);
        when(cloudinary.uploader().upload(any(), anyMap())).thenReturn(uploadResult);

        // When
        String imageUrl = cloudinaryService.uploadProfileImage(file);

        // Then
        assertEquals(uploadResult.get("url"), imageUrl);
        verify(cloudinary.uploader(), times(1)).upload(any(), anyMap());
    }

    @Test
    void givenFileLargerThan1MB_whenUploadProductImage_thenThrowsException() {
        Mockito.reset(cloudinary);
        
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getSize()).thenReturn(1024L * 1024 * 500);

        // When && Then
        assertThrows(CloudinaryException.class, () -> cloudinaryService.uploadProductImage(file, UUID.randomUUID().toString(), 0));
        verifyNoInteractions(cloudinary);
    }

    @Test
    void givenIOException_whenUploadProductImage_thenThrowsException() throws IOException {
        // Given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getSize()).thenReturn(1024L);
        when(cloudinary.uploader().upload(any(), anyMap())).thenThrow(IOException.class);

        // When && Then
        assertThrows(CloudinaryException.class, () -> cloudinaryService.uploadProductImage(file, UUID.randomUUID().toString(), 0));
    }

    @Test
    void givenHappyPath_whenUploadProductImage() throws IOException {
        //Given
        Map<String, String> uploadResult = Map.of("url", "www.image.com");
        MultipartFile file = mock(MultipartFile.class);
        when(file.getSize()).thenReturn(1024L);
        when(cloudinary.uploader().upload(any(), anyMap())).thenReturn(uploadResult);

        // When
        String imageUrl = cloudinaryService.uploadProductImage(file,UUID.randomUUID().toString(), 0);

        // Then
        assertEquals(uploadResult.get("url"), imageUrl);
        verify(cloudinary.uploader(), times(1)).upload(any(), anyMap());
    }

    @Test
    void givenImageUrl_whenCopyImageForOrder_thenReturnsSameUploadedUrl() throws IOException {
        // Given
        String productImageUrl = "product-image.com";
        Map<String, String> uploadResult = Map.of("url", productImageUrl);
        when(cloudinary.uploader().upload(any(), anyMap())).thenReturn(uploadResult);

        // When
        String savedUrl = cloudinaryService.copyImageForOrder(productImageUrl);

        // Then
        assertEquals(uploadResult.get("url"), savedUrl);
        verify(cloudinary.uploader(), times(1)).upload(any(), anyMap());
    }

    @Test
    void givenIOException_whenCopyImageForOrder_thenReturnsNull() throws IOException {
        //Given
        String productImageUrl = "product-image.com";
        when(cloudinary.uploader().upload(any(), anyMap())).thenThrow(IOException.class);

        // When && Then
        assertNull(cloudinaryService.copyImageForOrder(productImageUrl));
    }

    @Test
    void givenValidImageUrl_whenDeleteImage_thenUploaderDestroyCalled() throws Exception {
        // Given
        String imageUrl = "http://res.cloudinary.com/dpj028iin/image/upload/profile_pictures/sample_image.jpg";
        String expectedPublicId = "profile_pictures/sample_image";


        // When
        cloudinaryService.deleteImage(imageUrl);

        // Then
        verify(cloudinary.uploader(), times(1)).destroy(eq(expectedPublicId), anyMap());
    }

    @Test
    void givenInvalidImageUrl_whenDeleteImage_thenThrowsCloudinaryException() {
        // Given
        String invalidImageUrl = "http://example.com/image.jpg";

        // When & Then
        assertThrows(CloudinaryException.class, () -> cloudinaryService.deleteImage(invalidImageUrl));

        verifyNoInteractions(cloudinary.uploader());
    }

    @Test
    void givenValidImageUrl_whenUploaderThrowsIOException_thenThrowsCloudinaryException() throws Exception {
        // Given
        String imageUrl = "http://res.cloudinary.com/dpj028iin/image/upload/profile_pictures/sample_image.jpg";
        String expectedPublicId = "profile_pictures/sample_image";

        when(cloudinary.uploader().destroy(eq(expectedPublicId), anyMap())).thenThrow(IOException.class);

        // When & Then
        assertThrows(CloudinaryException.class, () -> cloudinaryService.deleteImage(imageUrl));

        verify(cloudinary.uploader(), times(1)).destroy(eq(expectedPublicId), anyMap());
    }

}
