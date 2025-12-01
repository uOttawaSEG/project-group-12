package com.uottawa.seg.group12otams;

import java.util.Date;

public class TimeUtils {
    // Returns true if two intervals [aStart, aEnd) and [bStart, bEnd) overlap
    public static boolean overlaps(Date aStart, Date aEnd, Date bStart, Date bEnd) {
        if (aStart == null || aEnd == null || bStart == null || bEnd == null) return false;
        return aStart.before(bEnd) && bStart.before(aEnd);
    }

    // Can cancel an approved session only if it starts in >= 24 hours from now
    public static boolean canCancelApproved(Date sessionStart, Date now) {
        if (sessionStart == null || now == null) return false;
        long diffMs = sessionStart.getTime() - now.getTime();
        long hours = diffMs / (1000L * 60L * 60L);
        return hours >= 24L;
    }
}
