package se.sitic.megatron.core;

import se.sitic.megatron.util.StringUtil;


/**
 * Defines an integer interval with a start- and end value.
 */
public class Interval implements Comparable<Interval> {
    private long start;
    private long end;
    
    
    public Interval(long start, long end) {
        this.start = start;
        this.end = end;
    }


    /**
     * Constructs an interval from specified string. Supports open
     * start and end, for example: "1-100", "-100", "50-". Only positive
     * numbers are valid. 
     */
    public Interval(String str) throws MegatronException {
        parse(str);
    }

    
    /**
     * Returns true if specified value is in this interval. 
     */
    public final boolean contains(long val) {
        return (val >= start) && (val <= end); 
    }

    
    /**
     * Returns true if specified interval intersects with this interval. 
     */
    public final boolean overlaps(Interval interval) {
        return contains(interval.getStart()) || contains(interval.getEnd()) ||
            interval.contains(start) || interval.contains(end);
    }
    
    
    public long getStart() {
        return start;
    }


    public long getEnd() {
        return end;
    }

    
    // implements Comparable
    public int compareTo(Interval interval2) {
        long start2 = interval2.getStart();
        long end2 = interval2.getEnd();
        
        if (start < start2) {
            return -1;
        } else if (start > start2) {
            return 1;
        } else {
            // start == start2
            return (end < end2) ? -1 : ((end == end2) ? 0 : 1);
        }
    }

    
    // generated by Eclipse
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (end ^ (end >>> 32));
        result = prime * result + (int) (start ^ (start >>> 32));
        return result;
    }

    
    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof Interval) {
            Interval interval2 = (Interval)obj;
            result = (start == interval2.getStart()) && (end == interval2.getEnd());
        }
        return result;
    }

    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (start != Long.MIN_VALUE) {
            result.append(start);
        }
        result.append('-');
        if (end != Long.MAX_VALUE) {
            result.append(end);
        }
        
        return result.toString();
    }

    
    private void parse(String str) throws MegatronException {
        if (StringUtil.isNullOrEmpty(str) || str.trim().equals("-")) {
            throw new MegatronException("Invalid interval: " + str);
        }
        
        String[] headTail = StringUtil.splitHeadTail(str, "-", false);
        if ((headTail == null) || str.equals(headTail[0])) {
            throw new MegatronException("Invalid interval; '-' not found: " + str);
        }

        try {
            if (headTail[0].length() > 0) {
                start = Long.parseLong(headTail[0]);
            } else {
                start = Long.MIN_VALUE;
            }
            if (headTail[1].length() > 0) {
                end = Long.parseLong(headTail[1]);
            } else {
                end = Long.MAX_VALUE;
            }
        } catch (NumberFormatException e) {
            throw new MegatronException("Invalid interval; cannot parse number: " + str, e);
        }
    }
    
}
