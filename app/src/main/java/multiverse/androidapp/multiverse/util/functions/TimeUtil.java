package multiverse.androidapp.multiverse.util.functions;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

import multiverse.androidapp.multiverse.R;

public class TimeUtil {

    public static final long MS_IN_MINUTE = 60000L;
    public static final long MS_IN_DAY = 86400000L;
    public static final long MS_IN_MONTH = 2628000000L;
    public static final long MS_IN_YEAR = 31540000000L;

    public static TimeDiff getTimeDiffFromNow(Date date) {
        TimeDiff info = new TimeDiff();
        Date now = Calendar.getInstance().getTime();
        long diffInMs = now.getTime() - date.getTime();
        info.nbrOfMinute = (int) (diffInMs / MS_IN_MINUTE);
        info.nbrOfDay = (int) (diffInMs / MS_IN_DAY);
        info.nbrOfMonth = (int) (diffInMs / MS_IN_MONTH);
        info.nbrOfYear = (int) (diffInMs / MS_IN_YEAR);

        return info;
    }

    public static class TimeDiff {
        public int nbrOfYear;
        public int nbrOfMonth;
        public int nbrOfDay;
        public int nbrOfMinute;

        public String toSimpleGraphicString(Context context) {
            String str = "";
            if (nbrOfYear > 0) {
                str = String.valueOf(nbrOfYear) + context.getString(R.string.general_year);
            } else if (nbrOfMonth > 0) {
                str = String.valueOf(nbrOfMonth) + context.getString(R.string.general_month);
            } else if (nbrOfDay > 0) {
                str = String.valueOf(nbrOfDay) + context.getString(R.string.general_day);
            } else if (nbrOfMinute > 0) {
                str = String.valueOf(nbrOfMinute) + context.getString(R.string.general_minute);
            } else {
                str = "0" + context.getString(R.string.general_minute);
            }
            return str;
        }
    }
}
