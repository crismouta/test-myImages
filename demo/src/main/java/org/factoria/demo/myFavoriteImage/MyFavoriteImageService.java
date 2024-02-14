package org.factoria.demo.myFavoriteImage;

import jakarta.transaction.Transactional;
import org.factoria.demo.myFavoriteImage.utils.IdWorker;
import org.factoria.demo.system.exception.ObjectByIdNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MyFavoriteImageService {
    private final MyFavoriteImageRepository myFavoriteImageRepository;

    private final IdWorker idWorker;

    public MyFavoriteImageService(MyFavoriteImageRepository myFavoriteImageRepository, IdWorker idWorker) {
        this.myFavoriteImageRepository = myFavoriteImageRepository;
        this.idWorker = idWorker;
    }

    public MyFavoriteImage findById(Long myImageId) {
        return this.myFavoriteImageRepository.findById(myImageId)
                .orElseThrow(() -> new ObjectByIdNotFoundException("MyFavoriteImage", myImageId));
    }

    public List<MyFavoriteImage> findAll() {
        return this.myFavoriteImageRepository.findAll();
    }

    public MyFavoriteImage save(MyFavoriteImage newImage) {
        newImage.setId(idWorker.nextId());
        return this.myFavoriteImageRepository.save(newImage);
    }

    public MyFavoriteImage update(Long imageId, MyFavoriteImage updatedImage) {
        return this.myFavoriteImageRepository.findById(imageId)
                .map(oldImage -> {
                    oldImage.setTitle(updatedImage.getTitle());
                    oldImage.setDescription(updatedImage.getDescription());
                    oldImage.setUrl(updatedImage.getUrl());
                    return this.myFavoriteImageRepository.save(oldImage);
                })
                .orElseThrow(() -> new ObjectByIdNotFoundException("MyFavoriteImage", imageId));
    }

    public void delete(Long myImageId) {
        this.myFavoriteImageRepository.findById(myImageId)
                .orElseThrow(() -> new ObjectByIdNotFoundException("MyFavoriteImage", myImageId));
        this.myFavoriteImageRepository.deleteById(myImageId);
    }
}
