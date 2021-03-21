package Usage_calculation;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class resultFormatter {

    private static DecimalFormat dateFormatter;

    private static boolean isBetween(double mid, double start, double end) {
        return start <= mid && mid < end;
    }

    public static String formatVal(double val) {
        String str;
        StringBuilder sb;
        dateFormatter = new DecimalFormat("#.#");
        if (isBetween(val, 0.0, 1024.0d)) {
            return "0 MB";
        }
        if (isBetween(val, 1024.0d, 1048576.0d)) {
            sb = new StringBuilder();
            sb.append(makeString(val / 1024.0d));
            str = " KB";
        } else if (isBetween(val, 1048576.0d, 1.073741824E9d)) {
            sb = new StringBuilder();
            sb.append(makeString(val / 1048576.0d));
            str = " MB";
        } else if (isBetween(val, 1.073741824E9d, 1.099511627776E12d)) {
            sb = new StringBuilder();
            sb.append(makeString(val / 1.073741824E9d));
            str = " GB";
        } else if (!isBetween(val, 1.099511627776E12d, 1.125899906842624E15d)) {
            sb = new StringBuilder();
            sb.append(makeString(val / 1.099511627776E12d));
            str = " TB";
        } else {
            return "";
        }
        sb.append(str);
        return sb.toString();
    }

    private static String makeString(double val) {
        dateFormatter.setRoundingMode(RoundingMode.UP);
        return dateFormatter.format(val);
    }
}
