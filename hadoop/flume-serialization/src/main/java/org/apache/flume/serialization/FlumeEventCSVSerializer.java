package org.apache.flume.serialization;

import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.TimestampInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlumeEventCSVSerializer implements EventSerializer {

    private final static Logger logger = LoggerFactory.getLogger(FlumeEventCSVSerializer.class);

    // dynamic values from flume conf file
    public static final String FORMAT = "format";
    public static final String REGEX = "regex";
    public static final String REGEX_ORDER = "regexorder";
    public static final String CATEGORY = "category";

    // Default values
    private final String DEFAULT_FORMAT = "CSV";
    private final String DEFAULT_REGEX = "(.*)";
    private final String DEFAULT_ORDER = "1";
    private final String DEFAULT_CATEGORY = "0";

    private final String format;
    private final String category;
    private final Pattern regex;
    private final String[] regexOrder;
    private final OutputStream out;
    private Matcher matcher;
    private Map<Integer, ByteBuffer > orderIndexer;
    private Map<String, ByteBuffer > result;

    public FlumeEventCSVSerializer(OutputStream out, Context context) {
        this.format = context.getString(FORMAT, DEFAULT_FORMAT);
        this.regex = Pattern.compile(context.getString(REGEX, DEFAULT_REGEX));
        this.regexOrder = context.getString(REGEX_ORDER, DEFAULT_ORDER).split(" ");
        this.out = out;
        this.orderIndexer = new HashMap<Integer, ByteBuffer>();
        this.result = new HashMap<String, ByteBuffer>();
        this.category = context.getString(CATEGORY, DEFAULT_CATEGORY);
    }

    @Override
    public void afterCreate() throws IOException {

    }

    @Override
    public void afterReopen() throws IOException {

    }

    @Override
    public void write(Event event) throws IOException {

        switch (category) {
            case "0" :
                writeAll(event);
                break;
            case "1":
                processResult(event);
                writeResult();
                break;
            case "2":
                processResult(event);
                writeResult();
                break;
            default:
                writeAll(event);
                break;
        }
    }

    private void alimOrderIndexer(Matcher matcher) {

        int groupIndex = 0;
        int totalGroups = matcher.groupCount();
        for (int i = 0, count = totalGroups; i < count; i++) {
            groupIndex = i + 1;
            orderIndexer.put(Integer.valueOf(regexOrder[i]), ByteBuffer.wrap(matcher.group(groupIndex).getBytes()));
        }
    }

    private void writeEvntCat_1(Event event) throws IOException {

        matcher = regex.matcher(new String(event.getBody(), Charsets.UTF_8));
        if (matcher.find()) {

            alimOrderIndexer(matcher);

            Iterator it = orderIndexer.keySet().iterator();

            writes(converterDateTime(IOUtils.toString(orderIndexer.get(it.next()).array(), "UTF-8")).getBytes(), ';');
            writes(converterDate(IOUtils.toString(orderIndexer.get(it.next()).array(), "UTF-8")).getBytes(), ';');
            writes(orderIndexer.get(it.next()).array(), ';');
            writes(orderIndexer.get(it.next()).array(), ';');
            writes(("NULL").getBytes(), ';');
            writes(("NULL").getBytes(), ';');
            writes(("NULL").getBytes(), ';');
            writes(("NULL").getBytes(), ';');
            writes(("NULL").getBytes(), ';');
            writes(("NULL").getBytes(), ';');
            writes(("NULL").getBytes(), ';');
            writes(("NULL").getBytes(), ';');
            writes(("NULL").getBytes(), ';');
            writes(("NULL").getBytes(), ';');
            writes(("NULL").getBytes(), ';');
            writes(("NULL").getBytes(), ';');
            writes(orderIndexer.get(it.next()).array(), ';');
            writes(orderIndexer.get(it.next()).array(), '\n');

        } else {
            logger.warn("Error in the event processing : " + IOUtils.toString(event.getBody()));
        }
    }

    private void writeAll(Event event) throws IOException {

        matcher = regex.matcher(new String(event.getBody(), Charsets.UTF_8));
        if (matcher.find()) {

            alimOrderIndexer(matcher);

            Iterator it = orderIndexer.keySet().iterator();
            out.write(orderIndexer.get(it.next()).array());

            while(it.hasNext()) {
                out.write(';');
                out.write(orderIndexer.get(it.next()).array());
            }
            out.write('\n');

        } else {
            logger.warn("Error in the event processing : " + IOUtils.toString(event.getBody()));
        }
    }

    private void processResult(Event event) throws IOException {

        result.clear();

        matcher = regex.matcher(new String(event.getBody(), Charsets.UTF_8));
        if (matcher.find()) {

            alimOrderIndexer(matcher);

            Object[] it = orderIndexer.keySet().toArray();
            int i = 0;

            for(Integer key : orderIndexer.keySet()) {

                if (Arrays.equals(orderIndexer.get(key).array(), "user".getBytes())) {
                    String tmp1 = new String(orderIndexer.get(it[i + 1]).array(), "UTF-8");
                    String[] tmp2;

                    if (tmp1.contains(":"))
                        tmp2 = tmp1.split(":");
                    else
                        tmp2 = tmp1.split("%");

                    result.put("cat_5", ByteBuffer.wrap(tmp2[1].getBytes()));
                    result.put("cat_6", ByteBuffer.wrap(tmp2[2].getBytes()));
                }

                if (Arrays.equals(orderIndexer.get(key).array(), "details".getBytes()))
                    result.put("cat_9", ByteBuffer.wrap(orderIndexer.get(it[i + 1]).array()));

                if (new String(orderIndexer.get(key).array()).contains("_movie_"))
                    result.put("cat_17", ByteBuffer.wrap(orderIndexer.get(key).array()));

                if (new String(orderIndexer.get(key).array()).contains("_cat_"))
                    result.put("cat_18", ByteBuffer.wrap(orderIndexer.get(key).array()));

                if (Arrays.equals(orderIndexer.get(key).array(), "media".getBytes())) {
                    String tmp1 = new String(orderIndexer.get(it[i + 1]).array(), "UTF-8");
                    tmp1 = tmp1.replace("<", "");
                    tmp1 = tmp1.replace(">", "");
                    result.put("cat_7", ByteBuffer.wrap(tmp1.getBytes()));
                }

                if (Arrays.equals(orderIndexer.get(key).array(), "universe".getBytes()))
                    result.put("cat_10", ByteBuffer.wrap(orderIndexer.get(it[i + 1]).array()));

                if (Arrays.equals(orderIndexer.get(key).array(), "catalogs".getBytes()))
                    result.put("cat_8", ByteBuffer.wrap(orderIndexer.get(it[i + 1]).array()));

                i++;
            }

            result.put("cat_1", ByteBuffer.wrap(converterDateTime(new String(orderIndexer.get(1).array(), "UTF-8")).getBytes()));
            result.put("cat_2", ByteBuffer.wrap(converterDate(new String(orderIndexer.get(2).array(), "UTF-8")).getBytes()));
            result.put("cat_3", ByteBuffer.wrap(orderIndexer.get(4).array()));
            result.put("cat_4", ByteBuffer.wrap(orderIndexer.get(3).array()));
            result.put("cat_20", ByteBuffer.wrap(orderIndexer.get(it[i - 2]).array()));
            result.put("cat_21", ByteBuffer.wrap(orderIndexer.get(it[i - 1]).array()));
        }
    }

    private void writeResult() throws IOException {

        writes(result.getOrDefault("cat_1", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_2", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_3", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_4", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_5", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_6", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_7", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_8", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_9", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_10", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_11", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_12", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_13", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_14", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_15", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_16", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_17", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_18", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_19", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_20", ByteBuffer.wrap("NULL".getBytes())).array(), ';');
        writes(result.getOrDefault("cat_21", ByteBuffer.wrap("NULL".getBytes())).array(), '\n');
    }

    private void writes(byte[] b, char pv) throws IOException {
        out.write(b);
        out.write(pv);
    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public void beforeClose() throws IOException {

    }

    @Override
    public boolean supportsReopen() {
        return true;
    }

    public static class Builder implements EventSerializer.Builder {
        @Override
        public EventSerializer build(Context context, OutputStream out) {
            FlumeEventCSVSerializer s = new FlumeEventCSVSerializer(out, context);
            return s;
        }
    }

    private String converterDate(String st) {

        DateFormat df1 = new SimpleDateFormat("dd/MMM/yyyy");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return df2.format(df1.parse(st)).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return st;
        }
    }

    private String converterDateTime(String st) {

        String[] s = st.split(" ");
        SimpleDateFormat df1 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.");

        try {
            return df2.format(df1.parse(s[0])).toString().concat(s[1].substring(1));
        } catch (ParseException e) {
            e.printStackTrace();
            return st;
        }
    }
}