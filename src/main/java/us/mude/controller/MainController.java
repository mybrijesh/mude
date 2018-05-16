package us.mude.controller;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import us.mude.common.GenreType;
import us.mude.common.GlobalProps;
import us.mude.common.MediaType;
import us.mude.common.RatingType;
import us.mude.model.Media;
import us.mude.model.Review;
import us.mude.repository.ArtistRepository;
import us.mude.repository.MediaRepository;
import us.mude.repository.MemberRepository;
import us.mude.repository.ReviewRepository;
import us.mude.util.DateManager;

@Controller
public class MainController {
    
    @Autowired
    MediaRepository mediaRepo;

    @Autowired
    MemberRepository memberRepo;

    @Autowired
    ArtistRepository artistRepo;

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    DateManager dm;

    private static final Logger log = LoggerFactory.getLogger(MediaController.class);

    @RequestMapping("/")
    public String goToindex(Model model) {
        String next = GlobalProps.HOME_PAGE;
        try {
            List<Media> comingSoon =
                    mediaRepo.findDistinctTop5ByMediaTypeAndReleaseDate_isComingSoonOrderByReleaseDate_TheatreAsc(
                            MediaType.MOVIE,
                            true
                    );
            List<Media> certified =
                    mediaRepo.findDistinctTop5ByMediaTypeOrderByMudemeter_criticMudemeterDesc(
                            MediaType.MOVIE
                    );
            List<Media> certifiedTV =
                    mediaRepo.findDistinctTop5ByMediaTypeOrderByMudemeter_criticMudemeterDesc(
                            MediaType.SEASON
                    );
            List<Media> topBoxOffice =
                    mediaRepo.findDistinctTop5ByOrderByBoxOfficeDesc();
            List<Media> topTVSeason =
                    mediaRepo.findDistinctTop5ByMediaTypeOrderByMudemeter_CriticAvgScoreDesc(
                            MediaType.SEASON
                    );
            List<Media> topMovie =
                    mediaRepo.findDistinctTop5ByMediaTypeOrderByMudemeter_CriticAvgScoreDesc(
                            MediaType.MOVIE
                    );
            List<Media> openThisWeek =
                    mediaRepo.findDistinctTop5ByMediaTypeAndReleaseDate_theatreGreaterThanEqualAndReleaseDate_theatreLessThanEqualOrderByReleaseDate_TheatreAsc(
                            MediaType.MOVIE,
                            dm.getCURRENT_WEEK_START_DATE(),
                            dm.getCURRENT_WEEK_END_DATE()
                    );
            List<Media> newTVTonight =
                    mediaRepo.findDistinctTop5ByMediaTypeAndReleaseDate_theatre(
                            MediaType.EPISODE,
                            dm.getCURRENT_DATE()
                    );
            List<Media> popularTV =
                    mediaRepo.findDistinctTop5ByMediaTypeOrderByReviewCountDesc(
                            MediaType.SEASON
                    );
            log.info("dm.getCURRENT_DATE(): "+ dm.getCURRENT_DATE());

            model.addAttribute("comingSoon", comingSoon);
            model.addAttribute("openThisWeek", openThisWeek);
            model.addAttribute("certified", certified);
            model.addAttribute("certifiedTV", certifiedTV);
            model.addAttribute("topBoxOffice", topBoxOffice);
            model.addAttribute("topTVSeason", topTVSeason);
            model.addAttribute("topMovie", topMovie);
            model.addAttribute("newTVTonight", newTVTonight);
            model.addAttribute("popularTV", popularTV);

            log.info("openThisWeek: " + openThisWeek.toString());
            log.info("certified: " + certified.toString());
            log.info("certifiedTV: " + certifiedTV.toString());
            log.info("topBoxOffice: " + topBoxOffice.toString());
            log.info("topTVSeason: " + topTVSeason.toString());
            log.info("topMovie: " + topMovie.toString());
            log.info("newTVTongiht: " + newTVTonight.toString());
            log.info("popularTV: " + popularTV.toString());

        } catch(Exception e) {
            e.printStackTrace();
            next = GlobalProps.SERVER_ERROR;
        }
        return next;
    }

    @RequestMapping("/browse")
    public String goToBrowse(Model model) {
        String next = GlobalProps.MOVIES_MAIN_PAGE;
        try {
            String genre = GlobalProps.ALL_GENRE;
            List<GenreType> genreTypes = new ArrayList();
            String[] genreList = genre.split(",");
            for(String g: genreList) {
                genreTypes.add(GenreType.valueOf(g));
            }
            Pageable page = PageRequest.of(Integer.parseInt(GlobalProps.PAGE_START), GlobalProps.BROWSE_ROW_COUNT);
            List<Media> comingSoon = mediaRepo.findDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
                    MediaType.MOVIE,
                    true,
                    0,
                    100,
                    genreTypes,
                    page
            );
            int totalCnt = mediaRepo.countDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
                    MediaType.MOVIE,
                    true,
                    0,
                    100,
                    genreTypes
            );
            model.addAttribute("comingSoon", comingSoon);
            model.addAttribute("totalCnt", totalCnt);
            log.info("totalCnt: "+ totalCnt);
        } catch(Exception e) {
            e.printStackTrace();
            next = GlobalProps.SERVER_ERROR;
        }
        return next;
    }

    @RequestMapping("/tv")
    public String goToTV(Model model) {
        String next = GlobalProps.TV_MAIN_PAGE;
        try {
        } catch(Exception e) {
            e.printStackTrace();
            next = GlobalProps.SERVER_ERROR;
        }
        return next;
    }

    @RequestMapping("/critics")
    public String goToCritics(Model model) {
        String next = GlobalProps.CRITICS_MAIN_PAGE;
        try {
        } catch(Exception e) {
            e.printStackTrace();
            next = GlobalProps.SERVER_ERROR;
        }
        return next;
    }

    @RequestMapping("/about")
    public String goToAbout(Model model) {
        String next = GlobalProps.ABOUT_PAGE;
        try {
        } catch(Exception e) {
            e.printStackTrace();
            next = GlobalProps.SERVER_ERROR;
        }
        return next;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @RequestMapping("/trailers")
    public String goToTrailers(Model model) {
        String next = GlobalProps.TRAILERS;
        try {
            List<Media> comingSoon =
                    mediaRepo.findDistinctTop10ByMediaTypeAndReleaseDate_isComingSoonOrderByReleaseDate_TheatreAsc(
                            MediaType.MOVIE,
                            true
                    );
            List<Media> certified =
                    mediaRepo.findDistinctTop10ByMediaTypeOrderByMudemeter_criticMudemeterDesc(
                            MediaType.MOVIE
                    );
            List<Media> certifiedTV =
                    mediaRepo.findDistinctTop10ByMediaTypeOrderByMudemeter_criticMudemeterDesc(
                            MediaType.SEASON
                    );
            List<Media> topBoxOffice =
                    mediaRepo.findDistinctTop10ByOrderByBoxOfficeDesc();
            List<Media> topTVSeason =
                    mediaRepo.findDistinctTop10ByMediaTypeOrderByMudemeter_CriticAvgScoreDesc(
                            MediaType.SEASON
                    );
            List<Media> topMovie =
                    mediaRepo.findDistinctTop10ByMediaTypeOrderByMudemeter_CriticAvgScoreDesc(
                            MediaType.MOVIE
                    );
            List<Media> openThisWeek =
                    mediaRepo.findDistinctTop10ByMediaTypeAndReleaseDate_theatreGreaterThanEqualAndReleaseDate_theatreLessThanEqualOrderByReleaseDate_TheatreAsc(
                            MediaType.MOVIE,
                            dm.getCURRENT_WEEK_START_DATE(),
                            dm.getCURRENT_WEEK_END_DATE()
                    );
            List<Media> newTVTonight =
                    mediaRepo.findDistinctTop10ByMediaTypeAndReleaseDate_theatre(
                            MediaType.EPISODE,
                            dm.getCURRENT_DATE()
                    );
            List<Media> popularTV =
                    mediaRepo.findDistinctTop10ByMediaTypeOrderByReviewCountDesc(
                            MediaType.SEASON
                    );
            log.info("dm.getCURRENT_DATE(): "+ dm.getCURRENT_DATE());

            model.addAttribute("comingSoon", comingSoon);
            model.addAttribute("openThisWeek", openThisWeek);
            model.addAttribute("certified", certified);
            model.addAttribute("certifiedTV", certifiedTV);
            model.addAttribute("topBoxOffice", topBoxOffice);
            model.addAttribute("topTVSeason", topTVSeason);
            model.addAttribute("topMovie", topMovie);
            model.addAttribute("newTVTonight", newTVTonight);
            model.addAttribute("popularTV", popularTV);

            log.info("openThisWeek: " + openThisWeek.toString());
            log.info("certified: " + certified.toString());
            log.info("certifiedTV: " + certifiedTV.toString());
            log.info("topBoxOffice: " + topBoxOffice.toString());
            log.info("topTVSeason: " + topTVSeason.toString());
            log.info("topMovie: " + topMovie.toString());
            log.info("newTVTongiht: " + newTVTonight.toString());
            log.info("popularTV: " + popularTV.toString());

        } catch(Exception e) {
            e.printStackTrace();
            next = GlobalProps.SERVER_ERROR;
        }
        return next;
    }
}
