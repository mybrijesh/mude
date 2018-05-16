package us.mude.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import us.mude.common.GenreType;
import us.mude.common.GlobalProps;
import us.mude.common.MediaType;
import us.mude.common.MemberType;
import us.mude.model.Artist;
import us.mude.model.Award;
import us.mude.model.Media;
import us.mude.model.Member;
import us.mude.repository.*;
import us.mude.util.DateManager;
import us.mude.util.MediaSpecification;
import us.mude.util.SearchCriteria;

import java.util.*;

@Controller
public class SearchController {

    @Autowired
    private MediaRepository mediaRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private AwardRepository awardRepo;

    @Autowired
    private DateManager dm;

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    @ResponseBody
    @RequestMapping("/searchbytyping")
    public Map searchByTyping(@RequestParam(name="keyword") String keyword) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            String[] tokens = keyword.split(" ");
            List<String> keywords = new ArrayList();
            Collections.addAll(keywords, tokens);

            List<Media> seasons =
                    mediaRepo.findDistinctTop5ByMediaTypeAndMediaNameIgnoreCaseContainingOrderByReleaseDate_TheatreDesc(
                            MediaType.SEASON,
                            keyword
                    );

            List<Media> movies =
                    mediaRepo.findDistinctTop5ByMediaTypeAndMediaNameIgnoreCaseContainingOrderByReleaseDate_TheatreDesc(
                            MediaType.MOVIE,
                            keyword
                    );

            List<Artist> artists =
                    artistRepo.findDistinctTop5ByName_firstNameInIgnoreCaseOrName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
                            keywords,
                            keywords
                    );

            List<Artist> suggestedArtists =
                    artistRepo.findDistinctByName_firstNameInIgnoreCaseAndName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
                            keywords,
                            keywords
                    );
            List<Artist> container = new ArrayList<>();
            if(suggestedArtists != null) {
                container.addAll(suggestedArtists);
            }
            container.addAll(artists);
            result.put("success", true);
            result.put("seasons", seasons);
            result.put("movies", movies);
            result.put("artists", container);

            log.info(suggestedArtists.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/search")
    public String search(
            @RequestParam(name="keyword") String keyword,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo,
            Model model) {
        String next = GlobalProps.SEARCH_PAGE;
        try {
            String[] tokens = keyword.split(" ");
            List<String> keywords = new ArrayList();
            Collections.addAll(keywords, tokens);
            List<Media> movies =
                    mediaRepo.findDistinctTop5ByMediaTypeAndMediaNameIgnoreCaseContainingOrderByReleaseDate_TheatreDesc(
                            MediaType.MOVIE,
                            keyword
                    );
            List<Media> tvSeries =
                    mediaRepo.findDistinctTop5ByMediaTypeAndMediaNameIgnoreCaseContainingOrderByReleaseDate_TheatreDesc(
                            MediaType.TVSERIES,
                            keyword
                    );
            List<Media> franchise =
                    mediaRepo.findDistinctTop5ByMediaTypeAndMediaNameIgnoreCaseContainingOrderByReleaseDate_TheatreDesc(
                            MediaType.FRANCHISE,
                            keyword
                    );
            List<Member> critics =
                    memberRepo.findDistinctTop5ByMemberTypeAndName_firstNameInIgnoreCaseOrName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
                            MemberType.CRITIC,
                            keywords,
                            keywords
                    );
            List<Artist> artists =
                    artistRepo.findDistinctTop5ByName_firstNameInIgnoreCaseOrName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
                            keywords,
                            keywords
                    );
            List<Artist> suggestedArtists =
                    artistRepo.findDistinctByName_firstNameInIgnoreCaseAndName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
                            keywords,
                            keywords
                    );
            List<Member> suggestedCritics =
                    memberRepo.findDistinctByMemberTypeAndName_firstNameInIgnoreCaseAndName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
                            MemberType.CRITIC,
                            keywords,
                            keywords
                    );

            List<Artist> aContainer = new ArrayList<>();
            if(suggestedArtists != null) {
                aContainer.addAll(suggestedArtists);
            }
            aContainer.addAll(artists);

            List<Member> cContainer = new ArrayList<>();
            if(suggestedCritics != null) {
                cContainer.addAll(suggestedCritics);
            }
            cContainer.addAll(critics);

            int mCnt = mediaRepo.countDistinctByMediaTypeAndMediaNameIgnoreCaseContaining(MediaType.MOVIE,keyword);
            int tvCnt = mediaRepo.countDistinctByMediaTypeAndMediaNameIgnoreCaseContaining(MediaType.TVSERIES,keyword);
            int fCnt = mediaRepo.countDistinctByMediaTypeAndMediaNameIgnoreCaseContaining(MediaType.FRANCHISE,keyword);
            int aCnt = artistRepo.countDistinctByName_firstNameInIgnoreCaseOrName_LastNameInIgnoreCase(keywords, keywords);
            int cCnt = memberRepo.countDistinctByMemberTypeAndName_firstNameInIgnoreCaseOrName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
                    MemberType.CRITIC,
                    keywords,
                    keywords
            );

            model.addAttribute("movies", movies);
            model.addAttribute("franchise", franchise);
            model.addAttribute("tvSeries", tvSeries);
            model.addAttribute("critics", cContainer);
            model.addAttribute("artists", aContainer);
            model.addAttribute("mCnt", mCnt);
            model.addAttribute("tvCnt", tvCnt);
            model.addAttribute("fCnt", fCnt);
            model.addAttribute("aCnt", aCnt);
            model.addAttribute("cCnt", cCnt);

            log.info(suggestedArtists.toString());
            log.info(suggestedCritics.toString());
        } catch(Exception e) {
            next = GlobalProps.SERVER_ERROR;
            e.printStackTrace();
        }
        return next;
    }

    @ResponseBody
    @RequestMapping("/searchmedia")
    public Map searchMedia(
            @RequestParam(name="keyword") String keyword,
            @RequestParam(name="mediaType") MediaType mediaType,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            String[] tokens = keyword.split(" ");
            List<String> keywords = new ArrayList();
            Collections.addAll(keywords, tokens);
            Pageable page = PageRequest.of(
                    pageNo,
                    GlobalProps.ROW_COUNT,
                    Sort.Direction.DESC,
                    "Mudemeter_criticMudemeter");
            List<Media> media =
                    mediaRepo.findDistinctByMediaTypeAndMediaNameIgnoreCaseContainingOrderByReleaseDate_TheatreDesc(
                            mediaType,
                            keyword,
                            page
                    );
            result.put("success", true);
            result.put("media", media);
            log.info(media.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/searchcritics")
    public Map searchCritics(
            @RequestParam(name="keyword") String keyword,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            String[] tokens = keyword.split(" ");
            List<String> keywords = new ArrayList();
            Collections.addAll(keywords, tokens);
            Pageable page = PageRequest.of(
                    pageNo,
                    GlobalProps.ROW_COUNT,
                    Sort.Direction.DESC,
                    "id");
            List<Member> critics =
                    memberRepo.findDistinctByMemberTypeAndName_firstNameInIgnoreCaseOrName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
                            MemberType.CRITIC,
                            keywords,
                            keywords,
                            page
                    );
            result.put("success", true);
            result.put("critics", critics);
            log.info(critics.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/searchartists")
    public Map searchArtists(
            @RequestParam(name="keyword") String keyword,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo,
            Model model) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            String[] tokens = keyword.split(" ");
            List<String> keywords = new ArrayList();
            Collections.addAll(keywords, tokens);
            Pageable page = PageRequest.of(
                    pageNo,
                    GlobalProps.ROW_COUNT,
                    Sort.Direction.DESC,
                    "id");
            List<Artist> artists =
                    artistRepo.findDistinctByName_firstNameInIgnoreCaseOrName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
                            keywords,
                            keywords,
                            page
                    );
            result.put("success", true);
            result.put("artists", artists);
            log.info(artists.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/statoverview")
    public Map getStatOverview() {
        Map result = new HashMap<String, List>();
        try {
            List<Media> comingSoon =
                    mediaRepo.findDistinctTop5ByReleaseDate_isComingSoonOrderByReleaseDate_TheatreAsc(
                            true
                    );
            List<Media> openThisWeek =
                    mediaRepo.findDistinctTop5ByMediaTypeAndReleaseDate_theatreGreaterThanEqualAndReleaseDate_theatreLessThanEqualOrderByReleaseDate_TheatreAsc(
                            MediaType.MOVIE,
                            dm.getCURRENT_WEEK_START_DATE(),
                            dm.getCURRENT_WEEK_END_DATE()
                    );
            List<Media> topBoxOffice =
                    mediaRepo.findDistinctTop5ByOrderByBoxOfficeDesc();

            result.put("success", true);
            result.put("comingSoon", comingSoon);
            result.put("openThisWeek", openThisWeek);
            result.put("topBoxOffice", topBoxOffice);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/statmovies")
    public Map getStatMovies() {
        Map result = new HashMap<String, List>();
        try {
            List<Media> topMovie =
                    mediaRepo.findDistinctTop5ByMediaTypeOrderByMudemeter_CriticAvgScoreDesc(
                            MediaType.MOVIE
                    );
            List<Media> certified =
                    mediaRepo.findDistinctTop5ByOrderByMudemeter_criticMudemeterDesc();

            result.put("success", true);
            result.put("topMovie", topMovie);
            result.put("certified", certified);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/intheatrenow")
    public Map getInTheatreNow() {
        Map result = new HashMap<String, List>();
        try {
            List<Media> inTheatreNow =
                mediaRepo.findDistinctTop5ByMediaTypeAndReleaseDate_isComingSoonOrderByReleaseDate_TheatreDesc(
                        MediaType.MOVIE,
                        false
                );
            result.put("success", true);
            result.put("inTheatreNow", inTheatreNow);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/stattvshows")
    public Map getStatTVShows() {
        Map result = new HashMap<String, List>();
        try {
            List<Media> currentTopTVs =
                    mediaRepo.findDistinctTop5ByMediaTypeOrderByMudemeter_CriticAvgScoreDesc(
                            MediaType.SEASON
                    );
            List<Media> certifiedTVs =
                    mediaRepo.findDistinctTop5ByMediaTypeOrderByMudemeter_criticMudemeterDesc(
                            MediaType.SEASON
                    );
            List<Media> popularTVs =
                    mediaRepo.findDistinctTop5ByMediaTypeOrderByReviewCountDesc(
                            MediaType.SEASON
                    );

            result.put("success", true);
            result.put("currentTopTVs", currentTopTVs);
            result.put("certifiedTVs", certifiedTVs);
            result.put("popularTVs", popularTVs);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/browse/openthisweek")
    public Map getOpeningThisWeek(
            @RequestParam(name="genre", defaultValue = GlobalProps.ALL_GENRE) String genre,
            @RequestParam(name="sortby",  defaultValue = GlobalProps.SORT_BY_RELEASE_DATE) String sort,
            @RequestParam(name="mudemeterStart", defaultValue = GlobalProps.MUDEMETER_START) float mudemeterStart,
            @RequestParam(name="mudemeterEnd", defaultValue = GlobalProps.MUDEMETER_END) float mudemeterEnd,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo) {

        log.info("sort: "+sort);
        log.info("genre: "+genre);
        log.info("mudemeterStart: "+mudemeterStart);
        log.info("mudemeterEnd: "+mudemeterEnd);
        Map result = new HashMap<String, List>();
        try {
            List<GenreType> genreTypes = new ArrayList();
            String[] genreList = genre.split(",");
            for(String g: genreList) {
                genreTypes.add(GenreType.valueOf(g));
            }
            int totalCnt = 0;
            List<Media> openThisWeek = null;
            Pageable page = PageRequest.of(pageNo, GlobalProps.BROWSE_ROW_COUNT);
            if(sort.equals(GlobalProps.SORT_BY_RELEASE_DATE)) {
                openThisWeek = mediaRepo.findDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
                        MediaType.MOVIE,
                        dm.getCURRENT_WEEK_START_DATE(),
                        dm.getCURRENT_WEEK_END_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
                        MediaType.MOVIE,
                        dm.getCURRENT_WEEK_START_DATE(),
                        dm.getCURRENT_WEEK_END_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_MUDE_METER)) {
                openThisWeek = mediaRepo.findDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
                        MediaType.MOVIE,
                        dm.getCURRENT_WEEK_START_DATE(),
                        dm.getCURRENT_WEEK_END_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
                        MediaType.MOVIE,
                        dm.getCURRENT_WEEK_START_DATE(),
                        dm.getCURRENT_WEEK_END_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_BOX_OFFICE)) {
                openThisWeek = mediaRepo.findDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDesc(
                        MediaType.MOVIE,
                        dm.getCURRENT_WEEK_START_DATE(),
                        dm.getCURRENT_WEEK_END_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDesc(
                        MediaType.MOVIE,
                        dm.getCURRENT_WEEK_START_DATE(),
                        dm.getCURRENT_WEEK_END_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_POPULARITY)) {
                openThisWeek = mediaRepo.findDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
                        MediaType.MOVIE,
                        dm.getCURRENT_WEEK_START_DATE(),
                        dm.getCURRENT_WEEK_END_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
                        MediaType.MOVIE,
                        dm.getCURRENT_WEEK_START_DATE(),
                        dm.getCURRENT_WEEK_END_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_APHABET)) {
                openThisWeek = mediaRepo.findDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
                        MediaType.MOVIE,
                        dm.getCURRENT_WEEK_START_DATE(),
                        dm.getCURRENT_WEEK_END_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndReleaseDate_TheatreGreaterThanEqualAndReleaseDate_TheatreLessThanEqualAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
                        MediaType.MOVIE,
                        dm.getCURRENT_WEEK_START_DATE(),
                        dm.getCURRENT_WEEK_END_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            }


            result.put("success", true);
            result.put("openThisWeek", openThisWeek);
            result.put("totalCnt", totalCnt);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/browse/comingsoon")
    public Map getComingSoon(
            @RequestParam(name="genre", defaultValue = GlobalProps.ALL_GENRE) String genre,
            @RequestParam(name="sortby",  defaultValue = GlobalProps.SORT_BY_RELEASE_DATE) String sort,
            @RequestParam(name="mudemeterStart", defaultValue = GlobalProps.MUDEMETER_START) float mudemeterStart,
            @RequestParam(name="mudemeterEnd", defaultValue = GlobalProps.MUDEMETER_END) float mudemeterEnd,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo) {

        Map result = new HashMap<String, List>();
        try {
            List<GenreType> genreTypes = new ArrayList();
            String[] genreList = genre.split(",");
            for(String g: genreList) {
                genreTypes.add(GenreType.valueOf(g));
            }
            int totalCnt = 0;
            List<Media> comingSoon = null;
            Pageable page = PageRequest.of(pageNo, GlobalProps.BROWSE_ROW_COUNT);
            if(sort.equals(GlobalProps.SORT_BY_RELEASE_DATE)) {
                comingSoon = mediaRepo.findDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
                        MediaType.MOVIE,
                        true,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
                        MediaType.MOVIE,
                        true,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_MUDE_METER)) {
                comingSoon = mediaRepo.findDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
                        MediaType.MOVIE,
                        true,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
                        MediaType.MOVIE,
                        true,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_BOX_OFFICE)) {
                comingSoon = mediaRepo.findDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDesc(
                        MediaType.MOVIE,
                        true,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDesc(
                        MediaType.MOVIE,
                        true,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_POPULARITY)) {
                comingSoon = mediaRepo.findDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
                        MediaType.MOVIE,
                        true,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
                        MediaType.MOVIE,
                        true,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_APHABET)) {
                comingSoon = mediaRepo.findDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
                        MediaType.MOVIE,
                        true,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndReleaseDate_isComingSoonAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
                        MediaType.MOVIE,
                        true,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            }
            result.put("success", true);
            result.put("comingSoon", comingSoon);
            result.put("totalCnt", totalCnt);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/browse/topboxoffice")
    public Map getTopBoxOffice(
            @RequestParam(name="genre", defaultValue = GlobalProps.ALL_GENRE) String genre,
            @RequestParam(name="sortby",  defaultValue = GlobalProps.SORT_BY_RELEASE_DATE) String sort,
            @RequestParam(name="mudemeterStart", defaultValue = GlobalProps.MUDEMETER_START) float mudemeterStart,
            @RequestParam(name="mudemeterEnd", defaultValue = GlobalProps.MUDEMETER_END) float mudemeterEnd,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo) {

        Map result = new HashMap<String, List>();
        try {
            List<GenreType> genreTypes = new ArrayList();
            String[] genreList = genre.split(",");
            for(String g: genreList) {
                genreTypes.add(GenreType.valueOf(g));
            }
            int totalCnt = 0;
            List<Media> boxOffice = null;
            Pageable page = PageRequest.of(pageNo, GlobalProps.BROWSE_ROW_COUNT);
            if(sort.equals(GlobalProps.SORT_BY_RELEASE_DATE)) {
                boxOffice = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDescReleaseDate_theatreAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDescReleaseDate_theatreAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_MUDE_METER)) {
                boxOffice = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDescMudemeter_criticMudemeterDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDescMudemeter_criticMudemeterDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_BOX_OFFICE)) {
                boxOffice = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_POPULARITY)) {
                boxOffice = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_APHABET)) {
                boxOffice = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            }
            result.put("success", true);
            result.put("boxOffice", boxOffice);
            result.put("totalCnt", totalCnt);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/browse/certifiedmovie")
    public Map getCertifiedMovies(
            @RequestParam(name="genre", defaultValue = GlobalProps.ALL_GENRE) String genre,
            @RequestParam(name="sortby",  defaultValue = GlobalProps.SORT_BY_RELEASE_DATE) String sort,
            @RequestParam(name="mudemeterStart", defaultValue = GlobalProps.MUDEMETER_START) float mudemeterStart,
            @RequestParam(name="mudemeterEnd", defaultValue = GlobalProps.MUDEMETER_END) float mudemeterEnd,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo) {

        if (mudemeterStart < GlobalProps.CERTIFIED_THRESHOLD) {
            mudemeterStart = 80;
        }
        List<Media> certified = null;
        Map result = new HashMap<String, List>();
        try {
            List<GenreType> genreTypes = new ArrayList();
            String[] genreList = genre.split(",");
            for(String g: genreList) {
                genreTypes.add(GenreType.valueOf(g));
            }
            int totalCnt = 0;
            Pageable page = PageRequest.of(pageNo, GlobalProps.BROWSE_ROW_COUNT);
            if(sort.equals(GlobalProps.SORT_BY_RELEASE_DATE)) {
                certified = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_MUDE_METER)) {
                certified = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_BOX_OFFICE)) {
                certified = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_POPULARITY)) {
                certified = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_APHABET)) {
                certified = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            }
            result.put("success", true);
            result.put("certified", certified);
            result.put("totalCnt", totalCnt);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/browse/heighestmovie")
    public Map getHeighestMovie(
            @RequestParam(name="genre", defaultValue = GlobalProps.ALL_GENRE) String genre,
            @RequestParam(name="sortby",  defaultValue = GlobalProps.SORT_BY_RELEASE_DATE) String sort,
            @RequestParam(name="mudemeterStart", defaultValue = GlobalProps.MUDEMETER_START) float mudemeterStart,
            @RequestParam(name="mudemeterEnd", defaultValue = GlobalProps.MUDEMETER_END) float mudemeterEnd,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo) {

        List<Media> heighest = null;
        Map result = new HashMap<String, List>();
        try {
            List<GenreType> genreTypes = new ArrayList();
            String[] genreList = genre.split(",");
            for(String g: genreList) {
                genreTypes.add(GenreType.valueOf(g));
            }
            int totalCnt = 0;
            Pageable page = PageRequest.of(pageNo, GlobalProps.BROWSE_ROW_COUNT);
            if(sort.equals(GlobalProps.SORT_BY_RELEASE_DATE)) {
                heighest = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescReleaseDate_theatreAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescReleaseDate_theatreAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_MUDE_METER)) {
                heighest = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescMudemeter_criticMudemeterDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescMudemeter_criticMudemeterDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_BOX_OFFICE)) {
                heighest = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescBoxOfficeDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescBoxOfficeDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_POPULARITY)) {
                heighest = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescReviewCountDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescReviewCountDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_APHABET)) {
                heighest = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescMediaNameAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescMediaNameAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            }
            result.put("success", true);
            result.put("heighest", heighest);
            result.put("totalCnt", totalCnt);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/browse/popularmovie")
    public Map getPopularMovie(
            @RequestParam(name="genre", defaultValue = GlobalProps.ALL_GENRE) String genre,
            @RequestParam(name="sortby",  defaultValue = GlobalProps.SORT_BY_RELEASE_DATE) String sort,
            @RequestParam(name="mudemeterStart", defaultValue = GlobalProps.MUDEMETER_START) float mudemeterStart,
            @RequestParam(name="mudemeterEnd", defaultValue = GlobalProps.MUDEMETER_END) float mudemeterEnd,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo) {

        Map result = new HashMap<String, List>();
        try {
            int totalCnt = 0;
            List<Media> popular = null;
            List<GenreType> genreTypes = new ArrayList();
            String[] genreList = genre.split(",");
            for(String g: genreList) {
                genreTypes.add(GenreType.valueOf(g));
            }
            Pageable page = PageRequest.of(pageNo, GlobalProps.BROWSE_ROW_COUNT);
            if(sort.equals(GlobalProps.SORT_BY_RELEASE_DATE)) {
                popular = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescReleaseDate_theatreAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescReleaseDate_theatreAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_MUDE_METER)) {
                popular = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescMudemeter_criticMudemeterDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescMudemeter_criticMudemeterDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_BOX_OFFICE)) {
                popular = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescBoxOfficeDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescBoxOfficeDesc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_POPULARITY)) {
                popular = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_APHABET)) {
                popular = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescMediaNameAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescMediaNameAsc(
                        MediaType.MOVIE,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            }
            result.put("success", true);
            result.put("popular", popular);
            result.put("totalCnt", totalCnt);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/browse/awardwinner")
    public Map getAwardWinners(
            @RequestParam(name="sortby",  defaultValue = GlobalProps.SORT_BY_RELEASE_DATE) String sort,
            @RequestParam(name="awardYear", defaultValue = GlobalProps.AWARD_YEAR) String awardYear,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo) {

        Map result = new HashMap<String, List>();
        try {
            List<Integer> awardYears = new ArrayList<>();
            if(awardYear.equals("ALL")) {
                for(int i = 1960; i < 2018; i++) {
                    awardYears.add(i);
                }
            } else {
                awardYears.add(Integer.parseInt(awardYear));
            }
            int totalCnt = 0;
            List<Award> awardWinners = null;
            Pageable page = PageRequest.of(pageNo, GlobalProps.BROWSE_ROW_COUNT);
            if(sort.equals(GlobalProps.SORT_BY_RELEASE_DATE)) {
                awardWinners = awardRepo.findByAwardYearInOrderByMedia_ReleaseDate_theatreAsc(
                        awardYears,
                        page
                );
                totalCnt = awardRepo.countByAwardYearInOrderByMedia_ReleaseDate_theatreAsc(
                        awardYears
                );
            } else if(sort.equals(GlobalProps.SORT_BY_MUDE_METER)) {
                awardWinners = awardRepo.findByAwardYearInOrderByMedia_Mudemeter_criticMudemeterDesc(
                        awardYears,
                        page
                );
                totalCnt = awardRepo.countByAwardYearInOrderByMedia_Mudemeter_criticMudemeterDesc(
                        awardYears
                );
            } else if(sort.equals(GlobalProps.SORT_BY_BOX_OFFICE)) {
                awardWinners = awardRepo.findByAwardYearInOrderByMedia_BoxOfficeDesc(
                        awardYears,
                        page
                );
                totalCnt = awardRepo.countByAwardYearInOrderByMedia_BoxOfficeDesc(
                        awardYears
                );
            } else if(sort.equals(GlobalProps.SORT_BY_POPULARITY)) {
                awardWinners = awardRepo.findByAwardYearInOrderByMedia_reviewCountDesc(
                        awardYears,
                        page
                );
                totalCnt = awardRepo.countByAwardYearInOrderByMedia_reviewCountDesc(
                        awardYears
                );
            }
            result.put("success", true);
            result.put("awardWinners", awardWinners);
            result.put("totalCnt", totalCnt);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }


    @ResponseBody
    @RequestMapping("/browse/newtvtonight")
    public Map getNewTVTongiht(
            @RequestParam(name="genre", defaultValue = GlobalProps.ALL_GENRE) String genre,
            @RequestParam(name="sortby",  defaultValue = GlobalProps.SORT_BY_RELEASE_DATE) String sort,
            @RequestParam(name="mudemeterStart", defaultValue = GlobalProps.MUDEMETER_START) float mudemeterStart,
            @RequestParam(name="mudemeterEnd", defaultValue = GlobalProps.MUDEMETER_END) float mudemeterEnd,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo) {

        Map result = new HashMap<String, List>();
        try {
            List<GenreType> genreTypes = new ArrayList();
            String[] genreList = genre.split(",");
            for(String g: genreList) {
                genreTypes.add(GenreType.valueOf(g));
            }
            int totalCnt =0;
            List<Media> newTVTonight = null;
            Pageable page = PageRequest.of(pageNo, GlobalProps.BROWSE_ROW_COUNT);
            if(sort.equals(GlobalProps.SORT_BY_RELEASE_DATE)) {
                newTVTonight = mediaRepo.findDistinctByMediaTypeAndReleaseDate_theatreAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
                        MediaType.EPISODE,
                        dm.getCURRENT_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndReleaseDate_theatreAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
                        MediaType.EPISODE,
                        dm.getCURRENT_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_MUDE_METER)) {
                newTVTonight = mediaRepo.findDistinctByMediaTypeAndReleaseDate_theatreAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
                        MediaType.EPISODE,
                        dm.getCURRENT_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndReleaseDate_theatreAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
                        MediaType.EPISODE,
                        dm.getCURRENT_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_POPULARITY)) {
                newTVTonight = mediaRepo.findDistinctByMediaTypeAndReleaseDate_theatreAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
                        MediaType.MOVIE,
                        dm.getCURRENT_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndReleaseDate_theatreAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
                        MediaType.MOVIE,
                        dm.getCURRENT_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_APHABET)) {
                newTVTonight = mediaRepo.findDistinctByMediaTypeAndReleaseDate_theatreAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
                        MediaType.MOVIE,
                        dm.getCURRENT_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndReleaseDate_theatreAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
                        MediaType.MOVIE,
                        dm.getCURRENT_DATE(),
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            }
            result.put("success", true);
            result.put("newTVTonight", newTVTonight);
            result.put("totalCnt", totalCnt);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/browse/certifiedtv")
    public Map getCertifiedTV(
            @RequestParam(name="genre", defaultValue = GlobalProps.ALL_GENRE) String genre,
            @RequestParam(name="sortby",  defaultValue = GlobalProps.SORT_BY_RELEASE_DATE) String sort,
            @RequestParam(name="mudemeterStart", defaultValue = GlobalProps.MUDEMETER_START) float mudemeterStart,
            @RequestParam(name="mudemeterEnd", defaultValue = GlobalProps.MUDEMETER_END) float mudemeterEnd,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo) {

        if (mudemeterStart < GlobalProps.CERTIFIED_THRESHOLD) {
            mudemeterStart = 80;
        }
        List<Media> certified = null;
        Map result = new HashMap<String, List>();
        try {
            int totalCnt = 0;
            List<GenreType> genreTypes = new ArrayList();
            String[] genreList = genre.split(",");
            for(String g: genreList) {
                genreTypes.add(GenreType.valueOf(g));
            }
            Pageable page = PageRequest.of(pageNo, GlobalProps.BROWSE_ROW_COUNT);
            if(sort.equals(GlobalProps.SORT_BY_RELEASE_DATE)) {
                certified = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReleaseDate_theatreAsc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_MUDE_METER)) {
                certified = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticMudemeterDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_BOX_OFFICE)) {
                certified = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByBoxOfficeDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_POPULARITY)) {
                certified = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_APHABET)) {
                certified = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            }
            result.put("success", true);
            result.put("certified", certified);
            result.put("totalCnt", totalCnt);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }


    @ResponseBody
    @RequestMapping("/browse/heighesttv")
    public Map getHeighestTV(
            @RequestParam(name="genre", defaultValue = GlobalProps.ALL_GENRE) String genre,
            @RequestParam(name="sortby",  defaultValue = GlobalProps.SORT_BY_RELEASE_DATE) String sort,
            @RequestParam(name="mudemeterStart", defaultValue = GlobalProps.MUDEMETER_START) float mudemeterStart,
            @RequestParam(name="mudemeterEnd", defaultValue = GlobalProps.MUDEMETER_END) float mudemeterEnd,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo) {

        List<Media> heighest = null;
        Map result = new HashMap<String, List>();
        try {
            List<GenreType> genreTypes = new ArrayList();
            String[] genreList = genre.split(",");
            for(String g: genreList) {
                genreTypes.add(GenreType.valueOf(g));
            }
            int totalCnt = 0;
            Pageable page = PageRequest.of(pageNo, GlobalProps.BROWSE_ROW_COUNT);
            if(sort.equals(GlobalProps.SORT_BY_RELEASE_DATE)) {
                heighest = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescReleaseDate_theatreAsc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescReleaseDate_theatreAsc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_MUDE_METER)) {
                heighest = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescMudemeter_criticMudemeterDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescMudemeter_criticMudemeterDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_BOX_OFFICE)) {
                heighest = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescBoxOfficeDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescBoxOfficeDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_POPULARITY)) {
                heighest = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescReviewCountDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescReviewCountDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_APHABET)) {
                heighest = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescMediaNameAsc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMudemeter_criticAvgScoreDescMediaNameAsc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            }
            result.put("success", true);
            result.put("heighest", heighest);
            result.put("totalCnt", totalCnt);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/browse/populartv")
    public Map getPopularTV(
            @RequestParam(name="genre", defaultValue = GlobalProps.ALL_GENRE) String genre,
            @RequestParam(name="sortby",  defaultValue = GlobalProps.SORT_BY_RELEASE_DATE) String sort,
            @RequestParam(name="mudemeterStart", defaultValue = GlobalProps.MUDEMETER_START) float mudemeterStart,
            @RequestParam(name="mudemeterEnd", defaultValue = GlobalProps.MUDEMETER_END) float mudemeterEnd,
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo) {

        List<Media> popular = null;
        Map result = new HashMap<String, List>();
        try {
            List<GenreType> genreTypes = new ArrayList();
            String[] genreList = genre.split(",");
            for(String g: genreList) {
                genreTypes.add(GenreType.valueOf(g));
            }
            int totalCnt = 0;
            Pageable page = PageRequest.of(pageNo, GlobalProps.BROWSE_ROW_COUNT);
            if(sort.equals(GlobalProps.SORT_BY_RELEASE_DATE)) {
                popular = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescReleaseDate_theatreAsc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescReleaseDate_theatreAsc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_MUDE_METER)) {
                popular = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescMudemeter_criticMudemeterDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescMudemeter_criticMudemeterDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_BOX_OFFICE)) {
                popular = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescBoxOfficeDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountDescBoxOfficeDesc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_POPULARITY)) {
                popular = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountAsc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByReviewCountAsc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            } else if(sort.equals(GlobalProps.SORT_BY_APHABET)) {
                popular = mediaRepo.findDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes,
                        page
                );
                totalCnt = mediaRepo.countDistinctByMediaTypeAndMudemeter_criticMudemeterGreaterThanEqualAndMudemeter_criticMudemeterLessThanEqualAndGenreInOrderByMediaNameAsc(
                        MediaType.SEASON,
                        mudemeterStart,
                        mudemeterEnd,
                        genreTypes
                );
            }
            result.put("success", true);
            result.put("popular", popular);
            result.put("totalCnt", totalCnt);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }
}
