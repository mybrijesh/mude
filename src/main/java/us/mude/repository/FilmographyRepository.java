package us.mude.repository;

import org.springframework.data.repository.CrudRepository;
import us.mude.common.ArtistType;
import us.mude.model.Artist;
import us.mude.model.Filmography;
import us.mude.model.Media;
import us.mude.model.WantToSee;

import java.util.List;
import java.util.Optional;

public interface FilmographyRepository extends CrudRepository<Filmography, Long> {

    List<Filmography> findByMedia(Media m);

    List<Filmography> findByArtistTypeAndMedia (
            ArtistType a,
            Media m
    );

    Optional<Filmography> findByMedia_idAndArtist_idAndArtistType(
            Long m,
            Long a,
            ArtistType at
    );

}
