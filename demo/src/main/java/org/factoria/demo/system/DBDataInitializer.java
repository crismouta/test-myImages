package org.factoria.demo.system;

import org.factoria.demo.imageUser.UserService;
import org.factoria.demo.myFavoriteImage.MyFavoriteImage;
import org.factoria.demo.myFavoriteImage.MyFavoriteImageRepository;
import org.factoria.demo.imageUser.ImageUser;
import org.factoria.demo.imageUser.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final MyFavoriteImageRepository myFavoriteImageRepository;

    private final UserService userService;

    public DBDataInitializer(MyFavoriteImageRepository myFavoriteImageRepository, UserService userService) {
        this.myFavoriteImageRepository = myFavoriteImageRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        //Images to be charged once the application starts
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

        MyFavoriteImage image3 = new MyFavoriteImage();
        image3.setId(Long.valueOf("3"));
        image3.setTitle("Image 3");
        image3.setDescription("Description image 3");
        image3.setUrl("image3 URL");

        MyFavoriteImage image4 = new MyFavoriteImage();
        image4.setId(Long.valueOf("4"));
        image4.setTitle("Image 4");
        image4.setDescription("Description image 4");
        image4.setUrl("image4 URL");

        MyFavoriteImage image5 = new MyFavoriteImage();
        image5.setId(Long.valueOf("5"));
        image5.setTitle("Image 5");
        image5.setDescription("Description image 5");
        image5.setUrl("image5 URL");

        //Users to be charged once the application starts
        ImageUser user1 = new ImageUser();
        user1.setId(Long.valueOf("1"));
        user1.setName("User1");
        user1.setSurname("Surname1");
        user1.setEmail("user1@gmail.com");
        user1.setPassword("123456");
        user1.setEnabled(true);
        user1.setRoles("user");
        user1.addImage(image1);
        user1.addImage(image2);

        ImageUser user2 = new ImageUser();
        user2.setId(Long.valueOf("2"));
        user2.setName("User2");
        user2.setSurname("Surname2");
        user2.setEmail("user2@gmail.com");
        user2.setPassword("654321");
        user2.setEnabled(true);
        user2.setRoles("admin");
        user2.addImage(image3);
        user2.addImage(image4);

        this.userService.save(user1);
        this.userService.save(user2);
        this.myFavoriteImageRepository.save(image5);
    }
}
