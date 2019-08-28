package utils;

import com.brillio.timetable.enums.DayOfWeek;

public class Utils {

    public static DayOfWeek convertIntToDayOfWeek(int value) {
        switch (value) {
            case 0:
                return DayOfWeek.MON;
            case 1:
                return DayOfWeek.TUE;
            case 2:
                return DayOfWeek.WED;
            case 3:
                return DayOfWeek.THU;
            case 4:
                return DayOfWeek.FRI;
        }

        return null;
    }

    public static int convertDayOfWeekToInt(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MON:
                return 0;
            case TUE:
                return 1;
            case WED:
                return 2;
            case THU:
                return 3;
            case FRI:
                return 4;
            default:
                return -1;
        }
    }
}
