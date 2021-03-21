package Usage_calculation;

import android.content.Context;

import com.example.datapacktracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Usage_calculation.models.dailyUsageModel;

public class makeCalenderInstances {

    private static List<dailyUsageModel> list;
    private static Calendar todayStart;
    private static Calendar todayEnd;
    private static Calendar cal;
    private static Calendar end;
    private static Calendar start;
    private static dailyUsageModel usageModel;

    private static dailyUsageModel makeModel(Calendar calendar, Calendar calendar2, String str) {
        try {
            usageModel = new dailyUsageModel();
            usageModel.setStart(calendar);
            usageModel.setEnd(calendar2);
            usageModel.setType(str);
            return usageModel;
        } catch (Exception unused) {
            return usageModel;
        }
    }

    public static List<dailyUsageModel> days(int index, Context context) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            list = new ArrayList<>();
            cal = Calendar.getInstance();
            todayStart = Calendar.getInstance();
            todayEnd = Calendar.getInstance();
            todayStart.add(Calendar.DATE, -1);
            todayStart.set(Calendar.HOUR_OF_DAY, 23);
            todayStart.set(Calendar.MINUTE, 59);
            todayEnd.add(Calendar.DATE, 1);
            todayEnd.set(Calendar.HOUR_OF_DAY, 0);
            todayEnd.set(Calendar.MINUTE, 1);
            end = todayEnd;
            if (index != 30) {
                list.add(makeModel(todayStart, todayEnd, context.getResources().getString(R.string.today)));
            } else {
                list.add(makeModel(todayStart, todayEnd, simpleDateFormat.format(cal.getTime())));
            }
            for (int i2 = 1; i2 < index; i2++) {
                todayStart = Calendar.getInstance();
                todayEnd = Calendar.getInstance();
                cal = Calendar.getInstance();
                int i3 = -i2;
                cal.add(Calendar.DATE, i3);
                todayStart.add(Calendar.DATE, i3 - 1);
                todayStart.set(Calendar.HOUR_OF_DAY, 23);
                todayStart.set(Calendar.MINUTE, 59);
                todayEnd.add(Calendar.DATE, i3 + 1);
                todayEnd.set(Calendar.HOUR_OF_DAY, 0);
                todayEnd.set(Calendar.MINUTE, 1);
                list.add(makeModel(todayStart, todayEnd, simpleDateFormat.format(cal.getTime())));
            }
            start = todayStart;
            list.add(makeModel(start, end, context.getResources().getString(R.string.total)));
            return list;
        } catch (Exception unused) {
            return list;
        }
    }

    public static List<dailyUsageModel> months(Context context) {
        try {
            list = new ArrayList<>();
            todayStart = Calendar.getInstance();
            todayEnd = Calendar.getInstance();
            todayStart.set(Calendar.DATE, 1);
            todayStart.set(Calendar.HOUR_OF_DAY, 0);
            todayStart.set(Calendar.MINUTE, 1);
            todayEnd.add(Calendar.HOUR_OF_DAY, 2);
            end = todayEnd;
            list.add(makeModel(todayStart, todayEnd, todayStart.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)));
            for (int i = 1; i < 6; i++) {
                todayStart = Calendar.getInstance();
                todayEnd = Calendar.getInstance();
                int i2 = -i;
                todayStart.add(Calendar.MONTH, i2);
                todayStart.set(Calendar.DATE, 1);
                todayStart.set(Calendar.HOUR_OF_DAY, 0);
                todayStart.set(Calendar.MINUTE, 1);
                todayEnd.add(Calendar.MONTH, i2);
                todayEnd.set(Calendar.DATE, todayEnd.getActualMaximum(Calendar.DATE));
                todayEnd.set(Calendar.HOUR_OF_DAY, 23);
                todayEnd.set(Calendar.MINUTE, 59);
                list.add(makeModel(todayStart, todayEnd, todayStart.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)));
            }
            start = todayStart;
            list.add(makeModel(start, end, context.getResources().getString(R.string.total)));
            return list;
        } catch (Exception unused) {
            return list;
        }
    }

    public static List<dailyUsageModel> years(Context context) {
        try {
            list = new ArrayList<>();
            todayStart = Calendar.getInstance();
            todayEnd = Calendar.getInstance();
            todayStart.set(Calendar.MONTH, 0);
            todayStart.set(Calendar.DATE, 1);
            todayStart.set(Calendar.HOUR_OF_DAY, 0);
            todayStart.set(Calendar.MINUTE, 1);
            todayEnd.add(Calendar.DATE, 1);
            todayEnd.set(Calendar.HOUR_OF_DAY, 0);
            todayEnd.set(Calendar.MINUTE, 1);
            end = todayEnd;
            list.add(makeModel(todayStart, todayEnd, Integer.toString(todayStart.get(Calendar.YEAR))));
            for (int i = 1; i < 3; i++) {
                todayStart = Calendar.getInstance();
                todayEnd = Calendar.getInstance();
                int i2 = -i;
                todayStart.add(Calendar.YEAR, i2);
                todayStart.set(Calendar.MONTH, 0);
                todayStart.set(Calendar.DATE, 1);
                todayStart.set(Calendar.HOUR_OF_DAY, 0);
                todayStart.set(Calendar.MINUTE, 1);
                todayEnd.add(Calendar.YEAR, i2 + 1);
                todayEnd.set(Calendar.MONTH, 0);
                todayEnd.set(Calendar.DATE, 1);
                todayEnd.set(Calendar.HOUR_OF_DAY, 0);
                todayEnd.set(Calendar.MINUTE, 1);
                list.add(makeModel(todayStart, todayEnd, Integer.toString(todayStart.get(Calendar.YEAR))));
            }
            start = todayStart;
            list.add(makeModel(start, end, context.getResources().getString(R.string.total)));
            return list;
        } catch (Exception unused) {
            return list;
        }
    }

    public static List<dailyUsageModel> yearList(Context context) {
        try {
            list = new ArrayList<>();
            todayStart = Calendar.getInstance();
            todayEnd = Calendar.getInstance();
            todayStart.add(Calendar.YEAR, -2);
            todayEnd.add(Calendar.HOUR_OF_DAY, 2);
            list.add(makeModel(todayStart, todayEnd, context.getResources().getString(R.string.overall_time)));
            return list;
        } catch (Exception unused) {
            return list;
        }
    }

    public static List<dailyUsageModel> weeks(Context context) {
        try {
            String[] strArr = {context.getResources().getString(R.string.this_week), context.getResources().getString(R.string.last_week), context.getResources().getString(R.string.two_week_ago), context.getResources().getString(R.string.three_week_ago), context.getResources().getString(R.string.four_week_ago)};
            list = new ArrayList();
            todayStart = Calendar.getInstance();
            todayEnd = Calendar.getInstance();
            todayStart.add(Calendar.DATE, -7);
            todayStart.set(Calendar.HOUR_OF_DAY, 23);
            todayStart.set(Calendar.MINUTE, 59);
            todayEnd.add(Calendar.DATE, 1);
            todayEnd.set(Calendar.HOUR_OF_DAY, 0);
            todayEnd.set(Calendar.MINUTE, 1);
            end = todayEnd;
            list.add(makeModel(todayStart, todayEnd, strArr[0]));
            for (int i = 7; i < 29; i += 7) {
                todayStart = Calendar.getInstance();
                todayEnd = Calendar.getInstance();
                int i2 = -i;
                todayStart.add(Calendar.DATE, i2 - 7);
                todayStart.set(Calendar.HOUR_OF_DAY, 23);
                todayStart.set(Calendar.MINUTE, 59);
                todayEnd.add(Calendar.DATE, i2 + 1);
                todayEnd.set(Calendar.HOUR_OF_DAY, 0);
                todayEnd.set(Calendar.MINUTE, 1);
                list.add(makeModel(todayStart, todayEnd, strArr[i / 7]));
            }
            start = todayStart;
            list.add(makeModel(start, end, context.getResources().getString(R.string.total)));
            return list;
        } catch (Exception unused) {
            return list;
        }
    }

    public static List<dailyUsageModel> dayList(Context context) {
        try {
            list = new ArrayList<>();
            todayStart = Calendar.getInstance();
            todayEnd = Calendar.getInstance();
            todayStart.add(Calendar.DATE, -1);
            todayStart.set(Calendar.HOUR_OF_DAY, 23);
            todayStart.set(Calendar.MINUTE, 59);
            todayEnd.add(Calendar.DATE, 1);
            todayEnd.set(Calendar.HOUR_OF_DAY, 0);
            todayEnd.set(Calendar.MINUTE, 1);
            list.add(makeModel(todayStart, todayEnd, context.getResources().getString(R.string.today)));
        } catch (Exception ignored) {
        }
        return list;
    }

    public static dailyUsageModel today(Context context) {
        try {
            todayStart = Calendar.getInstance();
            todayEnd = Calendar.getInstance();
            todayStart.add(Calendar.DATE, -1);
            todayStart.set(Calendar.HOUR_OF_DAY, 23);
            todayStart.set(Calendar.MINUTE, 59);
            todayEnd.add(Calendar.DATE, 1);
            todayEnd.set(Calendar.HOUR_OF_DAY, 0);
            todayEnd.set(Calendar.MINUTE, 1);
            return makeModel(todayStart, todayEnd, context.getResources().getString(R.string.today));
        } catch (Exception unused) {
            return makeModel(todayStart, todayEnd, context.getResources().getString(R.string.today));
        }
    }

    public static dailyUsageModel lastHour(Context context) {
        try {
            todayStart = Calendar.getInstance();
            todayEnd = Calendar.getInstance();
            todayEnd.add(Calendar.HOUR_OF_DAY, 2);
            todayStart.add(Calendar.HOUR_OF_DAY, -1);
            return makeModel(todayStart, todayEnd, context.getResources().getString(R.string.last_hour));
        } catch (Exception unused) {
            return makeModel(todayStart, todayEnd, context.getResources().getString(R.string.last_hour));
        }
    }

    public static dailyUsageModel last12Hours(Context context) {
        try {
            todayStart = Calendar.getInstance();
            todayEnd = Calendar.getInstance();
            todayStart.set(Calendar.HOUR_OF_DAY, -12);
            todayEnd.add(Calendar.HOUR_OF_DAY, 2);
            todayEnd.add(Calendar.DATE, 1);
            todayEnd.set(Calendar.HOUR_OF_DAY, 0);
            todayEnd.set(Calendar.MINUTE, 1);
            return makeModel(todayStart, todayEnd, context.getResources().getString(R.string.last_twelve_hours));
        } catch (Exception unused) {
            return makeModel(todayStart, todayEnd, context.getResources().getString(R.string.last_twelve_hours));
        }
    }

    public static dailyUsageModel yesterday(Context context) {
        try {
            todayStart = Calendar.getInstance();
            todayEnd = Calendar.getInstance();
            todayStart.add(Calendar.DATE, -2);
            todayStart.set(Calendar.HOUR_OF_DAY, 23);
            todayStart.set(Calendar.MINUTE, 59);
            todayEnd.set(Calendar.HOUR_OF_DAY, 0);
            todayEnd.set(Calendar.MINUTE, 0);
            todayEnd.set(Calendar.SECOND, 1);
            return makeModel(todayStart, todayEnd, context.getResources().getString(R.string.yesterday));
        } catch (Exception unused) {
            return makeModel(todayStart, todayEnd, context.getResources().getString(R.string.yesterday));
        }
    }

    public static dailyUsageModel weekly(Context context) {
        try {
            todayStart = Calendar.getInstance();
            todayEnd = Calendar.getInstance();
            todayEnd.add(Calendar.HOUR_OF_DAY, 2);
            todayStart.add(Calendar.DATE, -8);
            todayStart.set(Calendar.HOUR_OF_DAY, 23);
            todayStart.set(Calendar.MINUTE, 59);
            return makeModel(todayStart, todayEnd, context.getResources().getString(R.string.week));
        } catch (Exception unused) {
            return  makeModel(todayStart, todayEnd, context.getResources().getString(R.string.week));
        }
    }

    public static dailyUsageModel monthly(Context context) {
        try {
            todayStart = Calendar.getInstance();
            todayEnd = Calendar.getInstance();
            todayEnd.add(Calendar.HOUR_OF_DAY, 2);
            todayStart.set(Calendar.DATE, 1);
            todayStart.set(Calendar.HOUR_OF_DAY, 0);
            todayStart.set(Calendar.MINUTE, 1);
            return makeModel(todayStart, todayEnd, context.getResources().getString(R.string.month));
        } catch (Exception unused) {
            return makeModel(todayStart, todayEnd, context.getResources().getString(R.string.month));
        }
    }

    public static dailyUsageModel yearly(Context context) {
        try {
            todayStart = Calendar.getInstance();
            todayEnd = Calendar.getInstance();
            todayEnd.add(Calendar.HOUR_OF_DAY, 2);
            todayStart.add(Calendar.YEAR, -1);
            todayStart.set(Calendar.HOUR_OF_DAY, 23);
            todayStart.set(Calendar.MINUTE, 59);
            return makeModel(todayStart, todayEnd, context.getResources().getString(R.string.overall_time));
        } catch (Exception unused) {
            return makeModel(todayStart, todayEnd, context.getResources().getString(R.string.overall_time));
        }
    }
}
