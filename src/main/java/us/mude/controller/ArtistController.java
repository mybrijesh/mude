package us.mude.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import us.mude.common.ArtistType;
import us.mude.common.GenreType;
import us.mude.common.GlobalProps;
import us.mude.model.Artist;
import us.mude.model.Filmography;
import us.mude.model.Media;
import us.mude.repository.ArtistRepository;
import us.mude.repository.FilmographyRepository;
import us.mude.repository.MediaRepository;
import us.mude.repository.MemberRepository;

import java.util.*;

@Controller
public class ArtistController {

    @Autowired
    ArtistRepository artistRepo;

    @Autowired
    MediaRepository mediaRepo;

    @Autowired
    MemberRepository memberRepo;

    @Autowired
    FilmographyRepository filmoRepo;

    private static final Logger log = LoggerFactory.getLogger(ArtistController.class);

    @RequestMapping("/artist")
    public String viewArtistDetail(@RequestParam(name="artistID") Long artistID, Model model) {
        String next = GlobalProps.SERVER_ERROR;
        try {
            Optional<Artist> oa = artistRepo.findById(artistID);
            if(oa.isPresent()) {
                Artist a = oa.get();
                log.debug(a.toString());
                model.addAttribute("artist", a);
                model.addAttribute("heighest", getHeighest(a.getFilmography()));
                model.addAttribute("lowest", getLowest(a.getFilmography()));
                next = GlobalProps.ARTIST_DETAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return next;
    }

    @ResponseBody
    @RequestMapping("/artistinfo")
    public Map getArtistInfo(@RequestParam(name="artistID") Long artistID) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Optional<Artist> oa = artistRepo.findById(artistID);
            if (oa.isPresent()) {
                result.put("success", true);
                result.put("artist", oa.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/artistbykeyword")
    public Map getArtistInfo(@RequestParam(name="keyword") String keyword) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            String[] tokens = keyword.split(" ");
            List<String> keywords = new ArrayList();
            Collections.addAll(keywords, tokens);
            List<Artist> artists = artistRepo.findDistinctByName_firstNameInIgnoreCaseOrName_LastNameInIgnoreCaseOrderByName_firstNameAscName_lastNameAsc(
                    keywords,
                    keywords
            );
            result.put("success", true);
            result.put("artist", artists);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value={"/addfilmo", "/editfilmo"})
    public Map addFilmo(
            @RequestParam(name="mediaID") Long mediaID,
            @RequestParam(name="artistID") Long artistID,
            @RequestParam(name="artistType") ArtistType artistType,
            @RequestParam(name="artistRole") String artistRole) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Optional<Filmography> of = filmoRepo.findByMedia_idAndArtist_idAndArtistType(mediaID, artistID, artistType);
            Filmography filmo = null;
            if(of.isPresent()) {
                filmo = of.get();
                filmo.setArtistRole(artistRole);
                filmo.setArtistType(artistType);
                filmoRepo.save(filmo);
                result.put("success", true);
            } else {
                Optional<Media> om = mediaRepo.findById(mediaID);
                Optional<Artist> oa = artistRepo.findById(artistID);
                if(om.isPresent() && oa.isPresent()) {
                    filmo = new Filmography();
                    filmo.setMedia(om.get());
                    filmo.setArtist(oa.get());
                    filmo.setArtistType(artistType);
                    filmo.setArtistRole(artistRole);
                    filmoRepo.save(filmo);
                    result.put("success", true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/deletefilmo")
    public Map deleteFilmo(@RequestParam(name="filmoIDs") String filmoIDs) {
        log.info("filmoIDs: "+filmoIDs);
        List<Long> filmoIDList = new ArrayList();
        String[] fl = filmoIDs.split(",");
        Map result = new HashMap<String, List>();
        for(String f: fl) {
            try {
                Long id = Long.parseLong(f);
                log.info("id: "+ id);
                filmoRepo.deleteById(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result.put("success", true);
        return result;
    }

    public Media getHeighest(List<Media> subm) {
        Media cMedia = null;
        if(subm.size() > 0) {
            cMedia = subm.get(0);
            for(Media m: subm) {
                if(cMedia.getMudemeter().getCriticMudemeter()<m.getMudemeter().getCriticMudemeter()) {
                    cMedia = m;
                }
            }
        }
        return cMedia;
    }
    public Media getLowest(List<Media> subm) {
        Media cMedia = null;
        if(subm.size() > 0) {
            cMedia = subm.get(0);
            for(Media m: subm) {
                if(cMedia.getMudemeter().getCriticMudemeter()>m.getMudemeter().getCriticMudemeter()) {
                    cMedia = m;
                }
            }
        }
        return cMedia;
    }


}
