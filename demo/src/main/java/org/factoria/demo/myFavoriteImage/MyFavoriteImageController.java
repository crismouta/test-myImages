package org.factoria.demo.myFavoriteImage;

import jakarta.validation.Valid;
import org.factoria.demo.myFavoriteImage.converter.MyFavoriteImageDtoToMyFavoriteImageConverter;
import org.factoria.demo.myFavoriteImage.converter.MyFavoriteImageToMyFavoriteImageDtoConverter;
import org.factoria.demo.myFavoriteImage.dto.MyFavoriteImageDto;
import org.factoria.demo.system.Result;
import org.factoria.demo.system.StatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}")
public class MyFavoriteImageController {
    private final MyFavoriteImageService myFavoriteImageService;

    private final MyFavoriteImageDtoToMyFavoriteImageConverter myFavoriteImageDtoToMyFavoriteImageConverter;

    private final MyFavoriteImageToMyFavoriteImageDtoConverter myFavoriteImageToMyFavoriteImageDtoConverter;

    public MyFavoriteImageController(MyFavoriteImageService myFavoriteImageService, MyFavoriteImageDtoToMyFavoriteImageConverter myFavoriteImageDtoToMyFavoriteImageConverter, MyFavoriteImageToMyFavoriteImageDtoConverter myFavoriteImageToMyFavoriteImageDtoConverter) {
        this.myFavoriteImageService = myFavoriteImageService;
        this.myFavoriteImageDtoToMyFavoriteImageConverter = myFavoriteImageDtoToMyFavoriteImageConverter;
        this.myFavoriteImageToMyFavoriteImageDtoConverter = myFavoriteImageToMyFavoriteImageDtoConverter;
    }
    @GetMapping("/myImages/{myImageId}")
    public Result findMyImageById(@PathVariable Long myImageId){
        MyFavoriteImage foundImage = this.myFavoriteImageService.findById(myImageId);
        MyFavoriteImageDto myFavoriteImageDto = this.myFavoriteImageToMyFavoriteImageDtoConverter.convert(foundImage);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", myFavoriteImageDto);
    }

    @GetMapping("/images")
    public Result findAllImages() {
        List<MyFavoriteImage> foundImages = this.myFavoriteImageService.findAll();
        List<MyFavoriteImageDto> imagesDto = foundImages.stream()
                .map(this.myFavoriteImageToMyFavoriteImageDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", imagesDto);
    }

    @PostMapping("/images")
    public Result addImage(@Valid @RequestBody MyFavoriteImageDto myFavoriteImageDto) {
        MyFavoriteImage newImage = this.myFavoriteImageDtoToMyFavoriteImageConverter.convert(myFavoriteImageDto);
        MyFavoriteImage savedImage = this.myFavoriteImageService.save(newImage);

        MyFavoriteImageDto savedImageDto = this.myFavoriteImageToMyFavoriteImageDtoConverter.convert(savedImage);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedImageDto );
    }

    @PutMapping("/myImages/{myImageId}")
    public Result updateImage(@PathVariable Long myImageId, @Valid @RequestBody MyFavoriteImageDto myFavoriteImageDto) {
        MyFavoriteImage updatedImage = this.myFavoriteImageDtoToMyFavoriteImageConverter.convert(myFavoriteImageDto);
        MyFavoriteImage result = this.myFavoriteImageService.update(myImageId, updatedImage);

        MyFavoriteImageDto updatedImageDto = this.myFavoriteImageToMyFavoriteImageDtoConverter.convert(result);
        return new Result(true, StatusCode.SUCCESS, "Updated Success", updatedImageDto );
    }

    @DeleteMapping("/myImages/{myImageId}")
    public Result deleteImage(@PathVariable Long myImageId) {
        this.myFavoriteImageService.delete(myImageId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}

