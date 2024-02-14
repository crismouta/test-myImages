package org.factoria.demo.myFavoriteImage;

import org.factoria.demo.myFavoriteImage.utils.IdWorker;
import org.factoria.demo.imageUser.ImageUser;
import org.factoria.demo.system.exception.ObjectByIdNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class MyFavoriteImageServiceTest {

    @Mock
    MyFavoriteImageRepository myFavoriteImageRepository;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    MyFavoriteImageService myFavoriteImageService;

    List<MyFavoriteImage> images;

    @BeforeEach
    void setUp() {
        MyFavoriteImage image1 = new MyFavoriteImage();
        image1.setId(Long.valueOf("1"));
        image1.setTitle("Image 1");
        image1.setDescription("Description image 1");
        image1.setUrl("image1 URL");

        MyFavoriteImage image2 = new MyFavoriteImage();
        image2.setId(Long.valueOf("2"));
        image2.setTitle("Image 2");
        image2.setDescription("Description image 2");
        image2.setUrl("image2 URL");

        this.images = new ArrayList<>();
        this.images.add(image1);
        this.images.add(image2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        //Given
        ImageUser user1 = new ImageUser();
        user1.setId(Long.valueOf("1"));
        user1.setName("User");
        user1.setSurname("1");
        user1.setEmail("user1@gmail.com");
        user1.setPassword("123456");

        MyFavoriteImage image1 = new MyFavoriteImage();
        image1.setId(Long.valueOf("1"));
        image1.setTitle("Image 1");
        image1.setDescription("Description image 1");
        image1.setUrl("image1 URL");
        image1.setOwner(user1);

        given(myFavoriteImageRepository.findById(Long.valueOf("1"))).willReturn(Optional.of(image1));

        //When
        MyFavoriteImage returnedImage = myFavoriteImageService.findById(Long.valueOf("1"));

        //Then
        assertThat(returnedImage.getId()).isEqualTo(image1.getId());
        assertThat(returnedImage.getTitle()).isEqualTo(image1.getTitle());
        assertThat(returnedImage.getDescription()).isEqualTo(image1.getDescription());
        assertThat(returnedImage.getUrl()).isEqualTo(image1.getUrl());
        verify(myFavoriteImageRepository, times(1)).findById(Long.valueOf("1"));
    }

    @Test
    void testFindByIdNotFound() {
        //Given
        given(myFavoriteImageRepository.findById(Mockito.any(Long.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(()-> {
            MyFavoriteImage returnedImage = myFavoriteImageService.findById(Long.valueOf("1"));
        });

        //Then
        assertThat(thrown).isInstanceOf(ObjectByIdNotFoundException.class).hasMessage("Could not find MyFavoriteImage with id: 1");
        verify(myFavoriteImageRepository, times(1)).findById(Long.valueOf("1"));
    }

    @Test
    void testFindAllSuccess() {
        //Given
        given(myFavoriteImageRepository.findAll()).willReturn(this.images);

        //When
        List<MyFavoriteImage> actualImages = myFavoriteImageService.findAll();

        //Then
        assertThat(actualImages.size()).isEqualTo(this.images.size());
        verify(myFavoriteImageRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess(){
        //Given
        MyFavoriteImage newImage = new MyFavoriteImage();
        newImage.setTitle("New Image 1");
        newImage.setDescription("New Image 2 description");
        newImage.setUrl("New Image 2 URL");

        given(idWorker.nextId()).willReturn(123456L);
        given(myFavoriteImageRepository.save(newImage)).willReturn(newImage);

        //When
        MyFavoriteImage savedImage = myFavoriteImageService.save(newImage);

        //Then
        assertThat(savedImage.getId()).isEqualTo(123456L);
        assertThat(savedImage.getTitle()).isEqualTo("New Image 1");
        assertThat(savedImage.getDescription()).isEqualTo("New Image 2 description");
        assertThat(savedImage.getUrl()).isEqualTo("New Image 2 URL");
        verify(myFavoriteImageRepository, times(1)).save(newImage);
    }

    @Test
    void testUpdateSuccess() {
        //Given
        MyFavoriteImage oldImage = new MyFavoriteImage();
        oldImage.setId(Long.valueOf("1"));
        oldImage.setTitle("New Image 1");
        oldImage.setDescription("New Image 1 description");
        oldImage.setUrl("New Image 1 URL");

        MyFavoriteImage updatedImage = new MyFavoriteImage();
        updatedImage.setId(Long.valueOf("1"));
        updatedImage.setTitle("New Image 1");
        updatedImage.setDescription("New Image 1 description updated");
        updatedImage.setUrl("New Image 1 URL");

        given(myFavoriteImageRepository.findById(Long.valueOf("1"))).willReturn(Optional.of(oldImage));
        given(myFavoriteImageRepository.save(oldImage)).willReturn(oldImage);

        //When
        MyFavoriteImage result = myFavoriteImageService.update(Long.valueOf("1"), updatedImage);

        //Then
        assertThat(result.getId()).isEqualTo(updatedImage.getId());
        assertThat(result.getDescription()).isEqualTo(updatedImage.getDescription());
        verify(myFavoriteImageRepository, times(1)).findById(Long.valueOf("1"));
        verify(myFavoriteImageRepository, times(1)).save(oldImage);
    }

    @Test
    void testUpdateNotFound() {
        //Given
        MyFavoriteImage updatedImage = new MyFavoriteImage();
        updatedImage.setId(Long.valueOf("1"));
        updatedImage.setTitle("New Image 1");
        updatedImage.setDescription("New Image 1 description updated");
        updatedImage.setUrl("New Image 1 URL");

        given(myFavoriteImageRepository.findById(Long.valueOf("1"))).willReturn(Optional.empty());

        //When
        assertThrows(ObjectByIdNotFoundException.class, () -> {
            myFavoriteImageService.update(Long.valueOf("1"), updatedImage);
        });

        //Then
        verify(myFavoriteImageRepository, times(1)).findById(Long.valueOf("1"));
    }

    @Test
    void testDeleteSuccess() {
        //Given
        MyFavoriteImage image = new MyFavoriteImage();
        image.setId(Long.valueOf("1"));
        image.setTitle("Image 1");
        image.setDescription("Image 1 description");
        image.setUrl("Image 1 URL");

        given(myFavoriteImageRepository.findById(Long.valueOf("1"))).willReturn(Optional.of(image));
        doNothing().when(myFavoriteImageRepository).deleteById(Long.valueOf("1"));

        //When
        myFavoriteImageService.delete(Long.valueOf("1"));

        //Then
        verify(myFavoriteImageRepository, times(1)).deleteById(Long.valueOf("1"));
    }

    @Test
    void testDeleteNotFound() {
        //Given
        given(myFavoriteImageRepository.findById(Long.valueOf("1"))).willReturn(Optional.empty());

        //When
        assertThrows(ObjectByIdNotFoundException.class,() -> {
            myFavoriteImageService.delete(Long.valueOf("1"));
        });

        //Then
        verify(myFavoriteImageRepository, times(1)).findById(Long.valueOf("1"));
    }
}