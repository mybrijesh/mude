package us.mude.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import us.mude.common.*;
import us.mude.model.*;
import us.mude.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.security.MessageDigest;
import java.net.URLEncoder;
import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;

@Controller
public class MemberController {

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private MediaRepository mediaRepo;

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private WantToSeeRepository wtsRepo;

    @Autowired
    private TokenRepository tokenRepo;

    @Autowired
    private BCryptPasswordEncoder bEncoder;

    @Autowired
    private EmailController eController;

    @Autowired
    private CRequestRepository reqRepo;

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    @ResponseBody
    @RequestMapping(value="/chpic", method=RequestMethod.POST)
    public Map changePicture(
            @RequestParam(name="memberID") Long memberID,
            HttpSession seesion) {
      Map result = new HashMap<String, List>( );
      result.put( "success", false );
      try {
        Optional<Member> mem = memberRepo.findById( memberID );

        if( mem.isPresent( ) ) {
          Member m = mem.get( );
          m.setProfileImage( new URI( "resource/members/" + memberID ) );
          if( memberRepo.save( m ) != null ) {
              seesion.setAttribute("member", m);
            result.put( "success", true );

          }
        }
      } catch( Exception ex ) {
        ex.printStackTrace( );
      }

      return result;
    }

    @ResponseBody
    @RequestMapping(value="/chname", method=RequestMethod.POST)
    public String changeName(
        @RequestParam(name="memberID") Long memberID,
        @RequestParam(name="firstName") String firstName,
        @RequestParam(name="lastName") String lastName,
       HttpServletRequest request,
        HttpSession seesion) {
      String referer = request.getHeader( "Referer" );
      Optional<Member> om = memberRepo.findById( memberID );

      if( om.isPresent( ) ) {
        Member m = om.get( );
        m.setName( new Name( firstName, lastName ) );
        memberRepo.save( m );
          seesion.setAttribute("member", m);
      }

      return "redirect:" + referer;
    }

    @ResponseBody
    @RequestMapping(value="/signup", method=RequestMethod.POST)
    public Map addNewMember(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String firstName,
            @RequestParam String lastName) {

        try {
            InternetAddress addr = new InternetAddress(email);
            MessageDigest md = MessageDigest.getInstance( "SHA-256" );
            long unixEpochTime = System.currentTimeMillis( ) / 1000L;
            String qtoken = Base64.getEncoder( ).encodeToString( md.digest( Long.toString( unixEpochTime ).getBytes( "UTF-8" ) ) );
            String title = "Mude Registration E-mail";
            String content = "Hello "  + firstName  + "! Your activation link is http://"  + GlobalProps.HOSTNAME
                    + ":8888/validation?token=" + URLEncoder.encode( qtoken, "UTF-8");

            Optional<Member> om = memberRepo.findByEmail( addr );
            if(!om.isPresent()) {
                eController.sendVerificationMail(email, title, content);
                Member m = new Member(addr, firstName, lastName, bEncoder.encode(password));
                m.setActivated(false);
                if (memberRepo.save(m)== null) {
                    return Collections.singletonMap("result", false);
                }
                Token token = new Token( m, qtoken );
                if( tokenRepo.save( token ) != null ) {
                    return Collections.singletonMap("result", true);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Collections.singletonMap("result", false);
    }

    @ResponseBody
    @RequestMapping(value="/signin", method=RequestMethod.POST)
    public Map signin(Member visitor, HttpSession httpSession, Model model) {
        try {
            Optional<Member> om = memberRepo.findByEmail(visitor.getEmail());
            if(om.isPresent()) {
                Member m = om.get();
                if(m.isActivated()) {
                    if(bEncoder.matches(visitor.getPassword(), m.getPassword())) {
                        model.addAttribute("member", m);
                        httpSession.setAttribute("member", m);
                        httpSession.setMaxInactiveInterval(GlobalProps.SESSION_INTERVAL);
                        Map result = new HashMap<String, Object>();
                        result.put("member", m);
                        result.put("result", true);
                        return result;
                    }
                }

            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Collections.singletonMap("result", false);
    }

    @ResponseBody
    @RequestMapping(value="/validation")
    public Map validate( @RequestParam(name="token") String token ) {
        Optional<Token> om = tokenRepo.findByToken( token );
        if(om.isPresent()) {
            Token tok = om.get();
            Member member = tok.getMember();
            member.setActivated(true);
            if(memberRepo.save(member)==null) {
                return Collections.singletonMap("result", false);
            }
            tokenRepo.delete(tok);
            return Collections.singletonMap("result", true);
        }
        return Collections.singletonMap("result", false);
    }

    @ResponseBody
    @RequestMapping(value="/resendverification")
    public Map resendVerfication( @RequestParam(name="email") String email ) {
        try {
            InternetAddress addr = new InternetAddress(email);
            Optional<Member> om = memberRepo.findByEmail(addr);
            if(om.isPresent()) {
                Member member = om.get();
                Optional<Token> ot = tokenRepo.findByMember(member);
                if(ot.isPresent()) {
                    Token t = ot.get();
                    String title = "Mude Re-Verification E-mail";
                    String content = "Hello "  + member.getName().getFirstName()  + "! Your activation link is http://"  + GlobalProps.HOSTNAME
                            + ":8888/validation?token=" + URLEncoder.encode( t.getToken(), "UTF-8");
                    eController.sendVerificationMail(email, title, content);
                    return Collections.singletonMap("result", true);
                }

            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Collections.singletonMap("result", false);
    }

    @ResponseBody
    @RequestMapping(value="/signout")
    public String signout(SessionStatus status, HttpSession session) {
        session.removeAttribute("member");
        status.setComplete();
        return GlobalProps.HOME_PAGE;
    }

    @RequestMapping("/member")
    public String viewMemberDetail(@RequestParam(name="memberID") Long memberID, HttpSession session, Model model) {
        String next = GlobalProps.MEMBER_DETAIL;
        try {
            Optional<Member> om = memberRepo.findById(memberID);
            if(om.isPresent()) {
                boolean goToMember = false;
                Member m = om.get();
                log.info(m.toString());
                if(m.isActivated()) {
                    if(m.isPrivate()) {
                        Member sessionMember = (Member) session.getAttribute("member");
                        if(sessionMember != null) {
                            if (sessionMember.getId().equals(memberID)) {
                                goToMember =true;
                            } else {
                                return GlobalProps.PRIVATE;
                            }
                        } else {
                            return GlobalProps.PRIVATE;
                        }
                    } else {
                        goToMember =true;
                    }
                } else {
                    return GlobalProps.INACTIVE;
                }
                if (goToMember) {

                    int wtsCnt = wtsRepo.countByIsWantToSeeAndMemberID(true, m.getId());
                    int niCnt = wtsRepo.countByIsWantToSeeAndMemberID(false, m.getId());
                    int rCnt = reviewRepo.countByMember_id(m.getId());
                    Review heighestReviewdMovie = getHeighest(m.getRatingHistory(), MediaType.MOVIE);
                    Review heighestReviewdTV = getHeighest(m.getRatingHistory(), MediaType.SEASON);
                    Review lowestReviewdMovie = getLowest(m.getRatingHistory(), MediaType.MOVIE);
                    Review lowestReviewdTV = getLowest(m.getRatingHistory(), MediaType.SEASON);

                    model.addAttribute("member", m);
                    model.addAttribute("wtsCnt", wtsCnt);
                    model.addAttribute("niCnt", niCnt);
                    model.addAttribute("rCnt", rCnt);
                    model.addAttribute("heighestReviewdMovie", heighestReviewdMovie);
                    model.addAttribute("heighestReviewdTV", heighestReviewdTV);
                    model.addAttribute("lowestReviewdMovie", lowestReviewdMovie);
                    model.addAttribute("lowestReviewdTV", lowestReviewdTV);

                    log.info("heighestReviewdMovie: "+ heighestReviewdMovie);
                    log.info("heighestReviewdTV: "+ heighestReviewdTV);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            next = GlobalProps.SERVER_ERROR;
        }
        return next;
    }
    public Review getHeighest(List<Review> subm, MediaType mt) {
        try {
            if(subm.size() > 0) {
                Review hReview = null;
                for(Review r: subm) {
                    Media m = r.getMedia();
                    if(m.getMediaType() == mt) {
                        if(hReview == null) {
                            hReview = r;
                            continue;
                        }
                        if(hReview.getScore()<r.getScore()) {
                            hReview = r;
                        }
                    }
                }
                return hReview;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Review getLowest(List<Review> subm, MediaType mt) {
        try {
            if(subm.size() > 0) {
                Review hReview = null;
                for(Review r: subm) {
                    Media m = r.getMedia();
                    if(m.getMediaType() == mt) {
                        if(hReview == null) {
                            hReview = r;
                            continue;
                        }
                        if(hReview.getScore()>r.getScore()) {
                            hReview = r;
                        }
                    }
                }
                return hReview;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @ResponseBody
    @RequestMapping("/wanttosee")
    public Map reflectWantToSee(
            @RequestParam(name="mediaID") Long mediaID,
            @RequestParam(name="isWantToSee") boolean isWantToSee,
            HttpSession session) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member member = (Member) session.getAttribute("member");
            if(member != null) {
                log.info("mediaID: "+mediaID);
                log.info("memberID: "+member.getId());
                log.info("isWantToSee: "+isWantToSee);
                Optional<WantToSee> opt = wtsRepo.findByMediaIDAndMemberID(mediaID, member.getId());
                if (opt.isPresent()) {
                    log.info("1");
                    WantToSee wts = opt.get();
                    wts.setWantToSee(isWantToSee);
                    wtsRepo.save(wts);
                } else {
                    log.info("2");
                    wtsRepo.save(new WantToSee(mediaID, member.getId(), isWantToSee));
                }
                result.put("success", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/deletewts")
    public Map deleteWts(
            @RequestParam(name="mediaID") Long mediaID,
            HttpSession session) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member member = (Member) session.getAttribute("member");
            if(member != null) {
                Optional<WantToSee> ow = wtsRepo.findByMediaIDAndMemberID(mediaID, member.getId());
                if (ow.isPresent()) {
                    wtsRepo.delete(ow.get());
                    result.put("success", true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @Transactional
    @RequestMapping(value={"/postreview", "/editreview"})
    public Map postReview(
            @RequestParam(name="mediaID") Long mediaID,
            @RequestParam(name="score") float score,
            @RequestParam(name="comment") String comment,
            @RequestParam(name="externalLink", defaultValue = "") String externalLink,
            HttpSession session) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member member = (Member) session.getAttribute("member");
            if(member != null) {
                Optional<Review> opt = reviewRepo.findByMedia_idAndMember_id(mediaID, member.getId());
                if (opt.isPresent()) {
                    log.info("review: "+ reviewRepo.toString());
                    Review review = opt.get();
                    review.setComment(comment);
                    review.setScore(score);
                    reviewRepo.save(review);
                } else {
                    log.info("review: "+ member.getId()+" "+mediaID+" "+score+" "+comment+" "+member.getMemberType().ordinal());
                    reviewRepo.addReview(member.getId(), mediaID, score, comment, member.getMemberType().ordinal());
                }
                result.put("success", true);

                // Update Average
                Map ratings = updateRatings(member, mediaID);
                result.put("ratings", ratings);

                log.info("result: "+result);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/deletereview")
    public Map deleteReview(
            @RequestParam(name="mediaID") Long mediaID,
            HttpSession session) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member member = (Member) session.getAttribute("member");
            if(member != null) {
                Optional<Media> om = mediaRepo.findById(mediaID);
                if (om.isPresent()) {
                    Optional<Review> or = reviewRepo.findByMedia_idAndMember_id(om.get().getId(), member.getId());
                    if(or.isPresent()) {
                        log.info("or.get().getId(): "+ or.get().getId());
                        reviewRepo.deleteById(or.get().getId());
                        result.put("success", true);
                        // Update Average
                        Map ratings = updateRatings(member, mediaID);
                        result.put("ratings", ratings);
                        log.info("result: "+result);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/removereview")
    public Map removeReview(
            @RequestParam(name="reviewID") Long reviewID,
            HttpSession session) {
        log.info("reviewID: "+reviewID);
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member member = (Member) session.getAttribute("member");
            if(member != null) {
                Optional<Review> or = reviewRepo.findById(reviewID);
                if(or.isPresent()) {
                    Review review = or.get();
                    Member reviewMember = review.getMember();
                    log.info("review: "+review);

                    reviewRepo.delete(review);
                    result.put("success", true);

                    updateRatings(reviewMember, review.getMedia().getId());
                    log.info("result: "+result);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/myinfo")
    public Map getMyInfo(HttpSession session) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member m = (Member) session.getAttribute("member");
            if(m != null) {
                result.put("success", true);
                result.put("member", m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/updatemyinfo")
    public Map updateMyInfo(
            @RequestParam(name="firstName") String firstName,
            @RequestParam(name="lastName") String lastName,
            @RequestParam(name="email") String email,
            @RequestParam(name="desc") String desc,
            @RequestParam(name="password") String pwd,
            @RequestParam(name="isPrivate") boolean isPrivate,
            HttpSession session) {

        log.info("isPrivate: " + isPrivate);
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member m = (Member) session.getAttribute("member");
            if(m != null) {
                InternetAddress addr = new InternetAddress(email);
                Optional<Member> oe = memberRepo.findByEmail(addr);
                if(oe.isPresent()) {
                    if (bEncoder.matches(pwd, m.getPassword())) {
                        m.setDesc(desc);
                        m.setEmail(addr);
                        m.getName().setFirstName(firstName);
                        m.getName().setLastname(lastName);
                        m.setPassword(bEncoder.encode(pwd));
                        m.setPrivate(isPrivate);
                        memberRepo.save(m);
                        session.setAttribute("member", m);
                        result.put("success", true);
                        result.put("member", m);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/changepwd")
    public Map updateMyPwd(
            @RequestParam(name="currentPwd") String currentPwd,
            @RequestParam(name="newPwd") String newPwd,
            HttpSession session) {

        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member m = (Member) session.getAttribute("member");
            if(m != null) {
                if(bEncoder.matches(currentPwd, m.getPassword())) {
                    m.setPassword(bEncoder.encode(newPwd));
                    memberRepo.save(m);
                    session.setAttribute("member", m);
                    result.put("success", true);
                    result.put("member", m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/mouseoverprofile")
    public Map getProfileMouseOver(HttpSession session) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member m = (Member) session.getAttribute("member");
            if(m != null) {
                int wtsCnt = wtsRepo.countByIsWantToSeeAndMemberID(true, m.getId());
                int niCnt = wtsRepo.countByIsWantToSeeAndMemberID(false, m.getId());
                int rCnt = reviewRepo.countByMember_id(m.getId());
                result.put("success", true);
                result.put("wtsCnt", wtsCnt);
                result.put("niCnt", niCnt);
                result.put("rCnt", rCnt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/forgotpwd")
    public Map forgotPwd(
            @RequestParam(name="email") String email) {

        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {

            Optional<Member> om = memberRepo.findByEmail(new InternetAddress(email));
            if (om.isPresent()) {
                Member m = om.get();
                String tempPwd = bEncoder.encode("TEMP");
                String title = "Mude Password Reminder";
                String content = "Hello "  + m.getName().getFirstName()  + "! Your temporary password is " + tempPwd;

                m.setPassword(bEncoder.encode(tempPwd));
                memberRepo.save(m);
                eController.sendVerificationMail(email, title, content);
                result.put("success", true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/rmfromwts")
    public Map removeFromWantToSee(
        @RequestParam(name="mediaID") Long mediaID, 
        @RequestParam(name="memberID") Long memberID ) {
      Map result = new HashMap<String, List>( );
      result.put( "success", false );

      try {
        Optional<WantToSee> wts = wtsRepo.findByMediaIDAndMemberID( mediaID, memberID );

        if( wts.isPresent( ) ) {
          wtsRepo.delete( wts.get( ) );
          result.put( "success", true );
        }
      } catch( Exception ex ) {
        ex.printStackTrace( );
      }

      return result;
    }

    @ResponseBody
    @RequestMapping("/myreviewandwts")
    public Map getMyReviewAndWts(
            @RequestParam(name="mediaID") Long mediaID,
            HttpSession session) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member m = (Member) session.getAttribute("member");
            if(m != null) {
                Optional<Review> or = reviewRepo.findByMedia_idAndMember_id(mediaID, m.getId());
                Optional<WantToSee> owts = wtsRepo.findByMediaIDAndMemberID(mediaID, m.getId());
                if(or.isPresent()) {
                    Review myReview = or.get();
                    result.put("success", true);
                    result.put("myReview", myReview);
                    log.info("myReview: "+myReview.toString());
                }
                if(owts.isPresent()) {
                    WantToSee wts = owts.get();
                    result.put("success", true);
                    result.put("myWantToSee", wts);
                    log.info("myWantToSee: "+wts.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/reportreview")
    public Map reportReview(
            @RequestParam(name="reviewID") Long reviewID,
            HttpSession session) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member m = (Member) session.getAttribute("member");
            if(m != null) {
                Optional<Review> or = reviewRepo.findById(reviewID);
                if(or.isPresent()) {
                    Review review = or.get();
                    review.setReported(true);
                    reviewRepo.save(review);
                    result.put("success", true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private Map updateRatings(Member member, Long mediaID) {
        Map result = null;
        try {
            log.info("[updateRatings] mediaID: "+mediaID);
            log.info("[updateRatings] memberType: "+member.getMemberType());

            Double avgScore = 0.0;
            Optional<Double> oa = reviewRepo.getAvgScore(mediaID, member.getMemberType().ordinal());
            if(oa.isPresent()) {
                avgScore = oa.get();
            }

            float certifiedCnt = reviewRepo.getCertifiedCount(mediaID, member.getMemberType().ordinal());
            float totalCount = reviewRepo.getTotalCount(mediaID, member.getMemberType().ordinal());
            float mudemeter = 0;
            if(totalCount != 0) {
                mudemeter = (certifiedCnt/totalCount) * 100;
            }

            log.info("[updateRatings] avgScore: "+avgScore);
            log.info("[updateRatings] certifiedCnt: "+certifiedCnt);
            log.info("[updateRatings] totalCount: "+totalCount);
            log.info("[updateRatings] mudemeter: "+mudemeter);

            result = new HashMap<String, List>();
            if(member.getMemberType() == MemberType.CRITIC) {
                mediaRepo.updateCriticRatings(avgScore, mudemeter, mediaID);
                result.put("criticAvgScore", avgScore);
                result.put("criticMudemeter", mudemeter);
            } else {
                mediaRepo.updateAudienceRatings(avgScore, mudemeter, mediaID);
                result.put("audienceAvgScore", avgScore);
                result.put("audienceMudemeter", mudemeter);
            }

            Long rCnt = reviewRepo.getReviewTotal(mediaID);
            mediaRepo.updateReviewCount(mediaID, rCnt);

        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/reportedreviews")
    public Map getReportedReviews(HttpSession session) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member member = (Member) session.getAttribute("member");
            if(member != null) {
                List<Review> reported = reviewRepo.findByReported(true);
                result.put("success", true);
                result.put("reported", reported);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/removemember")
    public Map removeMember(
            @RequestParam(name="memberID") Long memberID,
            HttpSession session) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member member = (Member) session.getAttribute("member");
            if(member != null) {
                Optional<Member> om = memberRepo.findById(memberID);
                if (om.isPresent()) {
                    Member targetMember = om.get();
                    targetMember.setActivated(false);
                    memberRepo.save(targetMember);
                }
                result.put("success", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/deletemyprofile")
    public Map deleteMyPofile(SessionStatus status, HttpSession session) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member member = (Member) session.getAttribute("member");
            if(member != null) {
                member.setActivated(false);
                memberRepo.save(member);
                session.removeAttribute("member");
                status.setComplete();
                result.put("success", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/requestcritic")
    public Map requestCritic(HttpSession session) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member member = (Member) session.getAttribute("member");
            if(member != null) {
                CriticRequest creq = null;
                Optional<CriticRequest> oc = reqRepo.findByMember(member);
                if(!oc.isPresent()) {
                    creq = new CriticRequest(member, false);
                    reqRepo.save(creq);
                    result.put("success", true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/viewrequests")
    public Map viewRequests(HttpSession session) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member member = (Member) session.getAttribute("member");
            if(member != null) {
                List<CriticRequest> reqs = reqRepo.findByOrderByRegDateAsc();
                result.put("success", true);
                result.put("reqs", reqs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/acceptornot")
    public Map acceptOrNot(
            @RequestParam(name="memberID") Long memberID,
            @RequestParam(name="accepted") boolean accepted,
            HttpSession session) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member member = (Member) session.getAttribute("member");
            if(member != null) {
                Optional<CriticRequest> ocr = reqRepo.findByMember_id(memberID);
                CriticRequest cr = ocr.get();
                if(accepted) {
                    Member targetMember = cr.getMember();
                    targetMember.setMemberType(MemberType.CRITIC);
                    memberRepo.save(targetMember);
                }
                reqRepo.delete(cr);
                result.put("success", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/criticlist")
    public Map getCriticList(
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Pageable page = PageRequest.of(pageNo, GlobalProps.ROW_COUNT);
            List<Member> critics = memberRepo.findByMemberTypeOrderByName_firstNameAscName_lastNameAsc(
                    MemberType.CRITIC,
                    page
            );
            int totalCnt = memberRepo.countByMemberTypeOrderByName_firstNameAscName_lastNameAsc(
                    MemberType.CRITIC
            );
            result.put("success", true);
            result.put("critics", critics);
            result.put("totalCnt", totalCnt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/latestreviews")
    public Map getLatestReviews(
            @RequestParam(name="pageNo", defaultValue = GlobalProps.PAGE_START) int pageNo) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Pageable page = PageRequest.of(pageNo, GlobalProps.ROW_COUNT);
            List<Review> reviews = reviewRepo.findByRatingTypeOrderByRegDateDesc(
                    RatingType.CRITIC,
                    page
            );
            int totalCnt = reviewRepo.countByRatingTypeOrderByRegDateDesc(
                    RatingType.CRITIC
            );
            result.put("success", true);
            result.put("reviews", reviews);
            result.put("rTotalCnt", totalCnt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/criticsbyalphabet")
    public Map getCriticsByAlphabet(
            @RequestParam(name="alphabet") String alphabet) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            List<Member> critics = memberRepo.findByMemberTypeAndName_lastNameStartsWithOrderByName_firstNameAscName_lastNameAsc(
                    MemberType.CRITIC,
                    alphabet
            );
            result.put("success", true);
            result.put("critics", critics);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/topfivecritics")
    public Map getTopFiveCritics() {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            List<Integer> critics = memberRepo.findTop5Critics();
            List<Member> top5 = new ArrayList<>();
            for(Integer mid: critics) {
                top5.add(memberRepo.findById(mid.longValue()).get());
            }
            result.put("success", true);
            result.put("top5", top5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/setprivacy")
    public Map setPrivacy(
            @RequestParam(name="privacy") Boolean privacy,
            HttpSession session) {

        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member member = (Member) session.getAttribute("member");
            if (member != null) {
                memberRepo.updatePrivacy(member.getId(), privacy);
                result.put("success", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/refeshsession")
    public Map setPrivacy(HttpSession session) {
        Map result = new HashMap<String, List>();
        result.put("success", false);
        try {
            Member sMember = (Member) session.getAttribute("member");
            if (sMember != null) {
                Optional<Member> om = memberRepo.findById(sMember.getId());
                if(om.isPresent()) {
                    Member m = om.get();
                    session.setAttribute("member", m);
                }
                result.put("success", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
