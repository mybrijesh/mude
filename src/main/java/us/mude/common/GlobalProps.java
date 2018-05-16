package us.mude.common;

import org.springframework.stereotype.Component;

@Component
public class GlobalProps {

    public static final int SESSION_INTERVAL = 600; // 10 mins
    public static final String PAGE_START = "0";
    public static final int ROW_COUNT = 10;
    public static final int BROWSE_ROW_COUNT = 12;
    public static final int CERTIFIED_THRESHOLD = 80;
    public static final String AWARD_YEAR = "ALL";

    public static final String SERVER_ERROR = "error.html";
    public static final String PRIVATE = "private.html";
    public static final String INACTIVE = "inactive.html";
    public static final String TRAILERS = "trailers.html";

    public static final String HOME_PAGE = "index.html";
    public static final String MOVIES_MAIN_PAGE = "browse.html";
    public static final String TV_MAIN_PAGE = "tv.html";
    public static final String CRITICS_MAIN_PAGE = "critics.html";
    public static final String ABOUT_PAGE = "about.html";
    public static final String REVIEW_PAGE = "review.html";

    public static final String SEARCH_PAGE = "search.html";

    public static final String MEMBER_DETAIL = "member.html";
    public static final String MOVIE_DETAIL = "movie.html";
    public static final String TVSERIES_DETAIL = "tvseries.html";
    public static final String SEASON_DETAIL = "season.html";
    public static final String EPISODE_DETAIL = "episode.html";
    public static final String FRANCHISE_DETAIL = "franchise.html";
    public static final String ARTIST_DETAIL = "artist.html";

    public static final String POSTER = "photos/0.jpg";
    public static final String BANNER = "photos/0_heroImage.jpg";

    public static final String HOSTNAME = "localhost";
    public static final String MAILFROM = "registration@mail.mude.us";
    public static final String SYS_USR = "mude";
    public static final String SYS_PWD = "password";

    public static final String FS_URI = "http://23.94.27.164";

    public static final String MUDEMETER_START = "1";
    public static final String MUDEMETER_END = "99";

    public static final String ALL_GENRE = "OTHER,ACTION,ANIMATION,ARTFOREIGN,CLASSIC,COMEDY,DOCUMENTARY,DRAMA,HORROR,KIDSFAMILY,MYSTERY,ROMANCE,SFFANTASY";
    public static final String SORT_BY_RELEASE_DATE = "RELEASE_DATE";
    public static final String SORT_BY_MUDE_METER = "MUDEMETER";
    public static final String SORT_BY_BOX_OFFICE = "BOX_OFFICE";
    public static final String SORT_BY_POPULARITY = "POPULARITY";
    public static final String SORT_BY_APHABET = "ALPHABET";

    public static final String DEFAULT_ID = "-1";


}
