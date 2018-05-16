package us.mude.controller;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.mude.common.*;
import us.mude.model.*;
import us.mude.repository.*;
import us.mude.util.DateManager;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.servlet.http.HttpSession;
import java.awt.image.AreaAveragingScaleFilter;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MediaController {

    @Autowired
    private MediaRepository mediaRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private WantToSeeRepository wtsRepo;

    @Autowired
    private FilmographyRepository filmoRepo;

    private static final Logger log = LoggerFactory.getLogger(MediaController.class);

    @RequestMapping("/movie")
    public String viewMovieDetail(
            @RequestParam(name="mediaID") Long mediaID,
            HttpSession session,
            Model model) {
        String next = GlobalProps.MOVIE_DETAIL;
        try {
            Optional<Media> o = mediaRepo.findById(mediaID);
            if (o.isPresent()) {
                Media movie = o.get();
                List<Filmography> stars = filmoRepo.findByArtistTypeAndMedia(
                        ArtistType.CAST,
                        movie
                );
                model.addAttribute("currentMovie", movie);
                model.addAttribute("stars", stars);
                Member m = (Member) session.getAttribute("member");
                if(m != null) {
                    Optional<Review> or = reviewRepo.findByMedia_idAndMember_id(mediaID, m.getId());
                    Optional<WantToSee> owts = wtsRepo.findByMediaIDAndMemberID(mediaID, m.getId());
                    if(or.isPresent()) {
                        Review myReview = or.get();
                        model.addAttribute("myReview", myReview);
                        log.info("myReview: "+myReview.toString());
                    }
                    if(owts.isPresent()) {
                        WantToSee wts = owts.get();
                        model.addAttribute("myWantToSee", wts);
                        log.info("myWantToSee: "+wts.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            next = GlobalProps.SERVER_ERROR;
        }
        return next;
    }

    @RequestMapping("/tvseries")
    public String viewTVSeriesDetail(@RequestParam(name="mediaID") Long mediaID, Model model) {
        String next = GlobalProps.TVSERIES_DETAIL;
        try {
            Optional<Media> o = mediaRepo.findById(mediaID);
            if (o.isPresent()) {
                Media tvseries = o.get();
                log.info(tvseries.toString());
                model.addAttribute("currentTVSeries", tvseries);

                Media heighest= getHeighest(tvseries.getSubmedia(), MediaType.SEASON);
                model.addAttribute("heighest", heighest);

            }
        } catch (Exception e) {
            e.printStackTrace();
            next = GlobalProps.SERVER_ERROR;
        }
        return next;
    }

    @RequestMapping("/season")
    public String viewSeasonDetail(
            @RequestParam(name="mediaID") Long mediaID,
            HttpSession session,
            Model model) {
        String next = GlobalProps.SEASON_DETAIL;
        try {
            Optional<Media> o = mediaRepo.findById(mediaID);
            if (o.isPresent()) {
                Media season = o.get();
                log.info(season.toString());
                model.addAttribute("currentSeason", season);

                Member m = (Member) session.getAttribute("member");
                if(m != null) {
                    Optional<Review> or = reviewRepo.findByMedia_idAndMember_id(mediaID, m.getId());
                    Optional<WantToSee> owts = wtsRepo.findByMediaIDAndMemberID(mediaID, m.getId());
                    if(or.isPresent()) {
                        Review myReview = or.get();
                        model.addAttribute("myReview", myReview);
                    }
                    if(owts.isPresent()) {
                        WantToSee wts = owts.get();
                        model.addAttribute("myWantToSee", wts);
                    }
                }
            }
        }catch ( Exception e) {
            e.printStackTrace();
            next = GlobalProps.SERVER_ERROR;
        }
        return next;
    }

    @RequestMapping("/episode")
    public String viewEpisodeDetail(
            @RequestParam(name="mediaID") Long mediaID,
            HttpSession session,
            Model model) {
        String next = GlobalProps.EPISODE_DETAIL;
        try {
            Optional<Media> o = mediaRepo.findById(mediaID);
            if (o.isPresent()) {
                Media episode = o.get();
                log.info(episode.toString());
                model.addAttribute("currentEpisode", episode);

                Member m = (Member) session.getAttribute("member");
                if(m != null) {
                    Optional<Review> or = reviewRepo.findByMedia_idAndMember_id(mediaID, m.getId());
                    Optional<WantToSee> owts = wtsRepo.findByMediaIDAndMemberID(mediaID, m.getId());
                    if(or.isPresent()) {
                        Review myReview = or.get();
                        model.addAttribute("myReview", myReview);
                    }
                    if(owts.isPresent()) {
                        WantToSee wts = owts.get();
                        model.addAttribute("myWantToSee", wts);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            next = GlobalProps.SERVER_ERROR;
        }
        return next;
    }

    public Media getHeighest(List<Media> subm, MediaType mt) {
        try {
            if(subm.size() > 0) {
                Media cMedia = null;
                for(Media m: subm) {
                    if(m.getMediaType() == mt) {
                        if(cMedia == null) {
                            cMedia = m;
                            continue;
                        }
                        if(cMedia.getMudemeter().getCriticMudemeter()<m.getMudemeter().getCriticMudemeter()) {
                            cMedia = m;
                        }
                    }
                }
                return cMedia;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/franchise")
    public String viewFranchiseDetail(@RequestParam(name="mediaID") Long mediaID, Model model) {
        String next = GlobalProps.FRANCHISE_DETAIL;
        try {
            Optional<Media> o = mediaRepo.findById(mediaID);
            if (o.isPresent()) {
                Media franchise = o.get();
                log.info(franchise.toString());
                model.addAttribute("currentFranchise", franchise);

                Media highestMovie = getHeighest(franchise.getSubmedia(), MediaType.MOVIE);
                Media highestTV = getHeighest(franchise.getSubmedia(), MediaType.SEASON);
                model.addAttribute("highestMovie", highestMovie);
                model.addAttribute("highestTV", highestTV);
            }
        } catch (Exception e) {
            e.printStackTrace();
            next = GlobalProps.SERVER_ERROR;
        }
        return next;
    }

    @ResponseBody
    @RequestMapping("/refeshaudiencereviews")
    public Map refeshAudienceReviews(@RequestParam(name="mediaID") Long mediaID) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            List<Review> reviews =
                    reviewRepo.findTop6ByMedia_IdAndRatingTypeOrderByRegDateDesc(mediaID, RatingType.AUDIENCE);
            result.put("success", true);
            result.put("audienceReviews", reviews);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/refeshcriticreviews")
    public Map refeshCriticReviews(@RequestParam(name="mediaID") Long mediaID) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            List<Review> reviews =
                    reviewRepo.findTop6ByMedia_IdAndRatingTypeOrderByRegDateDesc(mediaID, RatingType.CRITIC);
            result.put("success", true);
            result.put("criticReviews", reviews);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/mediainfo")
    public Map getMediaInfo(@RequestParam(name="mediaID") Long mediaID) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Optional<Media> o = mediaRepo.findById(mediaID);
            if (o.isPresent()) {
                Media media = o.get();
                log.info(media.toString());
                result.put("success", true);
                result.put("media", media);
                result.put("parent", media.getParent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/allmedianame")
    public Map getAllMediaName(@RequestParam(name="keyword") String keyword) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {

            List<Media> movie = mediaRepo.findDistinctTop10ByMediaTypeAndMediaNameIgnoreCaseContainingOrderByMediaNameAsc(
                    MediaType.MOVIE,
                    keyword
            );
            List<Media> tvseries = mediaRepo.findDistinctTop10ByMediaTypeAndMediaNameIgnoreCaseContainingOrderByMediaNameAsc(
                    MediaType.TVSERIES,
                    keyword
            );
            List<Media> season = mediaRepo.findDistinctTop10ByMediaTypeAndMediaNameIgnoreCaseContainingOrderByMediaNameAsc(
                    MediaType.SEASON,
                    keyword
            );
            List<Media> episode = mediaRepo.findDistinctTop10ByMediaTypeAndMediaNameIgnoreCaseContainingOrderByMediaNameAsc(
                    MediaType.EPISODE,
                    keyword
            );

            result.put("success", true);
            result.put("movie", movie);
            result.put("tvseries", tvseries);
            result.put("season", season);
            result.put("episode", episode);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value={"/editmedia", "/addmedia"})
    public Map saveMedia(
        @RequestParam(name="id", defaultValue=GlobalProps.DEFAULT_ID) Long mediaID,
        @RequestParam(name="parentID", defaultValue=GlobalProps.DEFAULT_ID) Long parentID,
        @RequestParam(name="mediaName") String mediaName,
        @RequestParam(name="mediaType") MediaType mediaType,
        @RequestParam(name="mediaRating") FilmRatingType filmRatingType,
        @RequestParam(name="releaseDate") String theatre_date,
        @RequestParam(name="dvdreleaseDate") String dvd_date,
        @RequestParam(name="runtime") int runtime,
        @RequestParam(name="boxOffice") int boxOffice,
        @RequestParam(name="desc") String desc,
        @RequestParam(name="genres") String genres
    ){
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {

            log.info("mediaID: "+mediaID);
            log.info("parentID: "+parentID);
            log.info("mediaName: "+mediaName);
            log.info("mediaType: "+mediaType);
            log.info("filmRatingType: "+filmRatingType);
            log.info("theatre_date: "+theatre_date);
            log.info("dvd_date: "+dvd_date);
            log.info("runtime: "+runtime);
            log.info("boxOffice: "+boxOffice);
            log.info("desc: "+desc);
            log.info("genres: "+genres);

            List<GenreType> genreList = new ArrayList();
            String[] splitted = genres.split(",");
            for(String s: splitted) {
                genreList.add(GenreType.valueOf(s));
            }

            Money money = Money.of(boxOffice, Monetary.getCurrency("USD"));
            Media media = null;

            if (mediaID <= -1) {
                media = new Media();
                media.setReviewCount(Long.parseLong("0"));
            } else {
                Optional<Media> om = mediaRepo.findById(mediaID);
                if(om.isPresent()) {
                    media = om.get();
                }
            }

            media.setBoxOffice(money);
            media.setMediaName(mediaName);
            media.setMediaType(mediaType);
            media.setFilmRating(filmRatingType);
            media.setGenre(genreList);
            media.setRuntime(runtime);
            media.setDesc(desc);

            SimpleDateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
            log.info(fmt.parse(theatre_date).toString());
            log.info(fmt.parse(dvd_date).toString());

            media.getReleaseDate().setTheatre(fmt.parse(theatre_date));
            media.getReleaseDate().setDvd(fmt.parse(dvd_date));

            if(0<parentID) {
                Optional<Media> om = mediaRepo.findById(parentID);
                if(om.isPresent()) {
                    media.setParent(om.get());
                }
            }

            log.info(media.toString());
            mediaRepo.save(media);
            result.put("success", true);



        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/getfilmo")
    public Map getFilmography(@RequestParam(name="mediaID") Long mediaID) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Optional<Media> om = mediaRepo.findById(mediaID);
            if (om.isPresent()) {
                List<Filmography> filmo = filmoRepo.findByMedia(om.get());
                result.put("success", true);
                result.put("filmo", filmo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/removemedia")
    public Map removeMedia(@RequestParam(name="mediaID") Long mediaID) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Optional<Media> om = mediaRepo.findById(mediaID);
            if (om.isPresent()) {
                mediaRepo.delete(om.get());
                result.put("success", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @RequestMapping("/media/review")
    public String getReviews(
            @RequestParam(name="mediaID") Long mediaID,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo,
            Model model) {
        String next = GlobalProps.REVIEW_PAGE;
        try {

            Pageable page = PageRequest.of(pageNo, GlobalProps.ROW_COUNT);
            List<Review> allCritics = reviewRepo.findByRatingTypeAndMedia_idOrderByRegDateDesc(
                    RatingType.CRITIC,
                    mediaID,
                    page
            );
            model.addAttribute("allCritics", allCritics);

        } catch(Exception e) {
            e.printStackTrace();
            next = GlobalProps.SERVER_ERROR;
        }
        return next;
    }
    @ResponseBody
    @RequestMapping("/media/allcritics")
    public Map getAllCritics(
            @RequestParam(name="mediaID") Long mediaID,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo,
            Model model) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Pageable page = PageRequest.of(pageNo, GlobalProps.ROW_COUNT);
            List<Review> allCritics = reviewRepo.findByRatingTypeAndMedia_idOrderByRegDateDesc(
                    RatingType.CRITIC,
                    mediaID,
                    page
            );
            result.put("success", true);
            result.put("allCritics", allCritics);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/media/topcritics")
    public Map getTopCritics(
            @RequestParam(name="mediaID") Long mediaID,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo,
            Model model) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Pageable page = PageRequest.of(pageNo, GlobalProps.ROW_COUNT);
            List<Review> topCritics = reviewRepo.findByRatingTypeAndMember_TopCriticAndMedia_idOrderByRegDateDesc(
                    RatingType.CRITIC,
                    true,
                    mediaID,
                    page
            );
            result.put("success", true);
            result.put("topCritics", topCritics);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/media/audience")
    public Map getAudienceReviews(
            @RequestParam(name="mediaID") Long mediaID,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo,
            Model model) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Pageable page = PageRequest.of(pageNo, GlobalProps.ROW_COUNT);
            List<Review> audience = reviewRepo.findByRatingTypeAndMedia_idOrderByRegDateDesc(
                    RatingType.AUDIENCE,
                    mediaID,
                    page
            );
            result.put("success", true);
            result.put("audience", audience);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
