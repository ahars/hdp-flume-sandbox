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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

    public FlumeEventCSVSerializer(OutputStream out, Context context) {
        this.format = context.getString(FORMAT, DEFAULT_FORMAT);
        this.regex = Pattern.compile(context.getString(REGEX, DEFAULT_REGEX));
        this.regexOrder = context.getString(REGEX_ORDER, DEFAULT_ORDER).split(" ");
        this.out = out;
        this.orderIndexer = new HashMap<Integer, ByteBuffer>();
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
                writeEvntCat_1(event);
                break;
            case "2":
                writeEvntCat_2(event);
                break;
            case "3":
                writeEvntCat_3(event);
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

    private void writeEvntCat_2(Event event) throws IOException {

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

    private void writeEvntCat_3(Event event) throws IOException {

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

    private void writeEvntCat_4(Event event) throws IOException {

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
            writes(orderIndexer.get(it.next()).array(), ';');
            writes(event.getHeaders().get(TimestampInterceptor.Constants.TIMESTAMP).getBytes(), '\n');

        } else {
            logger.warn("Error in the event processing : " + IOUtils.toString(event.getBody()));
        }
    }

    private void writeEvntCat_5(Event event) throws IOException {

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

    private void writeEvntCat_6(Event event) throws IOException {

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

    private void writeEvntCat_7(Event event) throws IOException {

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
