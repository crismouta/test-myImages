package org.factoria.demo.myFavoriteImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyFavoriteImageRepository extends JpaRepository<MyFavoriteImage, Long> {

}
