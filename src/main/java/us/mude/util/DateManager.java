package us.mude.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class DateManager {

    private Date CURRENT_DATE;
    private Date CURRENT_WEEK_END_DATE;
    private Date CURRENT_WEEK_START_DATE;

    public DateManager() {
        try{
            CURRENT_DATE = new Date();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            CURRENT_DATE = fmt.parse(fmt.format(CURRENT_DATE));
            Calendar c = Calendar.getInstance();
            c.setTime(CURRENT_DATE);

            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
            c.add(Calendar.DAY_OF_MONTH, - dayOfWeek);
            this.CURRENT_WEEK_START_DATE = c.getTime();

            c.add(Calendar.DAY_OF_MONTH, 6);
            this.CURRENT_WEEK_END_DATE = c.getTime();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Date getCURRENT_WEEK_END_DATE() {
        return CURRENT_WEEK_END_DATE;
    }

    public void setCURRENT_WEEK_END_DATE(Date CURRENT_WEEK_END_DATE) {
        this.CURRENT_WEEK_END_DATE = CURRENT_WEEK_END_DATE;
    }

    public Date getCURRENT_WEEK_START_DATE() {
        return CURRENT_WEEK_START_DATE;
    }

    public void setCURRENT_WEEK_START_DATE(Date CURRENT_WEEK_START_DATE) {
        this.CURRENT_WEEK_START_DATE = CURRENT_WEEK_START_DATE;
    }

    public Date getCURRENT_DATE() {
        return CURRENT_DATE;
    }

    public void setCURRENT_DATE(Date CURRENT_DATE) {
        this.CURRENT_DATE = CURRENT_DATE;
    }
}
