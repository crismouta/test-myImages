package org.factoria.demo.myFavoriteImage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.factoria.demo.myFavoriteImage.dto.MyFavoriteImageDto;
import org.factoria.demo.system.StatusCode;
import org.factoria.demo.system.exception.ObjectByIdNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class MyFavoriteImageControllerTest {

    @Autowired
    MockMvc myMockMvc;
    @MockBean
    MyFavoriteImageService myFavoriteImageService;

    @Autowired
    ObjectMapper objectMapper;

    List<MyFavoriteImage> images;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.images = new ArrayList<>();

        MyFavoriteImage image1 = new MyFavoriteImage();
        image1.setId(Long.valueOf("1"));
        image1.setTitle("Image 1");
        image1.setDescription("Description image 1");
        image1.setUrl("image1 URL");
        this.images.add(image1);

        MyFavoriteImage image2 = new MyFavoriteImage();
        image2.setId(Long.valueOf("2"));
        image2.setTitle("Image 2");
        image2.setDescription("Description image 2");
        image2.setUrl("image2 URL");
        this.images.add(image2);

        MyFavoriteImage image3 = new MyFavoriteImage();
        image3.setId(Long.valueOf("3"));
        image3.setTitle("Image 3");
        image3.setDescription("Description image 3");
        image3.setUrl("image3 URL");
        this.images.add(image3);

        MyFavoriteImage image4 = new MyFavoriteImage();
        image4.setId(Long.valueOf("4"));
        image4.setTitle("Image 4");
        image4.setDescription("Description image 4");
        image4.setUrl("image4 URL");
        this.images.add(image4);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser(authorities = { "ADMIN", "USER" })
    void testFindMyImageByIdSuccess() throws Exception {
        //Given
        given(this.myFavoriteImageService.findById(Long.valueOf("1"))).willReturn(this.images.get(0));

        //When and then
        this.myMockMvc.perform(get(this.baseUrl + "/myImages/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.title").value("Image 1"))
                .andExpect(jsonPath("$.data.id").value(Long.valueOf("1")));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN", "USER" })
    void testFindMyImageByIdNotFound() throws Exception {
        //Given
        given(this.myFavoriteImageService.findById(Long.valueOf("1"))).willThrow(new ObjectByIdNotFoundException("MyFavoriteImage", Long.valueOf("1")));

        //When and then
        this.myMockMvc.perform(get(this.baseUrl + "/myImages/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find MyFavoriteImage with id: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllImagesSuccess() throws Exception {
        //Given
        given(this.myFavoriteImageService.findAll()).willReturn(this.images);

        //When and then
        this.myMockMvc.perform(get(this.baseUrl + "/images").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.images.size())))
                .andExpect(jsonPath("$.data[0].id").value(Long.valueOf("1")))
                .andExpect(jsonPath("$.data[0].title").value("Image 1"))
                .andExpect(jsonPath("$.data[1].id").value(Long.valueOf("2")))
                .andExpect(jsonPath("$.data[1].title").value("Image 2"));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN", "USER" })
    void testSaveImageSuccess() throws Exception {
        //Given
        MyFavoriteImageDto myFavoriteImageDto = new MyFavoriteImageDto(null,
                "New Image",
                "New Image description",
                "New Image URL",
                null);
        String json = this.objectMapper.writeValueAsString(myFavoriteImageDto);

        MyFavoriteImage savedImage = new MyFavoriteImage();
        savedImage.setId(Long.valueOf("1"));
        savedImage.setTitle("New Image");
        savedImage.setDescription("New Image description");
        savedImage.setUrl("New Image URL");

        given(this.myFavoriteImageService.save(Mockito.any(MyFavoriteImage.class))).willReturn(savedImage);

        //When and then
        this.myMockMvc.perform(post(this.baseUrl + "/images").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.title").value(savedImage.getTitle()))
                .andExpect(jsonPath("$.data.description").value(savedImage.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedImage.getUrl()));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN", "USER" })
    void testUpdateImageSuccess() throws Exception {
        //Given
        MyFavoriteImageDto myFavoriteImageDto = new MyFavoriteImageDto(Long.valueOf("1"),
                "Image 1",
                "Image 1 description updated",
                "Image 1 URL",
                null);
        String json = this.objectMapper.writeValueAsString(myFavoriteImageDto);

        MyFavoriteImage updatedImage = new MyFavoriteImage();
        updatedImage.setId(Long.valueOf("1"));
        updatedImage.setTitle("Image 1");
        updatedImage.setDescription("Image 1 description updated");
        updatedImage.setUrl("Image 1 URL");

        given(this.myFavoriteImageService.update(eq(Long.valueOf("1")), Mockito.any(MyFavoriteImage.class))).willReturn(updatedImage);

        //When and then
        this.myMockMvc.perform(put(this.baseUrl + "/myImages/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Updated Success"))
                .andExpect(jsonPath("$.data.id").value(Long.valueOf("1")))
                .andExpect(jsonPath("$.data.title").value(updatedImage.getTitle()))
                .andExpect(jsonPath("$.data.description").value(updatedImage.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updatedImage.getUrl()));
    }

    @Test
    @WithMockUser(authorities = { "ADMIN", "USER" })
    void testUpdateImageErrorWithNonExistentId() throws Exception {
        //Given
        MyFavoriteImageDto myFavoriteImageDto = new MyFavoriteImageDto(Long.valueOf("1"),
                "Image 1",
                "Image 1 description updated",
                "Image 1 URL",
                null);
        String json = this.objectMapper.writeValueAsString(myFavoriteImageDto);

        given(this.myFavoriteImageService.update(eq(Long.valueOf("1")), Mockito.any(MyFavoriteImage.class))).willThrow(new ObjectByIdNotFoundException("MyFavoriteImage", Long.valueOf("1")));

        //When and then
        this.myMockMvc.perform(put(this.baseUrl + "/myImages/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find MyFavoriteImage with id: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN", "USER" })
    void testDeleteImageSuccess() throws Exception {
        //Given
        doNothing().when(this.myFavoriteImageService).delete(Long.valueOf("1"));

        //When and then
        this.myMockMvc.perform(delete(this.baseUrl + "/myImages/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(authorities = { "ADMIN", "USER" })
    void testDeleteImageErrorWithNonExistentId() throws Exception {
        //Given
        doThrow(new ObjectByIdNotFoundException("MyFavoriteImage", Long.valueOf("1"))).when(this.myFavoriteImageService).delete(Long.valueOf("1"));

        //When and then
        this.myMockMvc.perform(delete(this.baseUrl + "/myImages/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find MyFavoriteImage with id: 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}