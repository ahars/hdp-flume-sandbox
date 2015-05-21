package org.apache.flume.serialization;

import com.google.common.base.Charsets;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.TimestampInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlumeEventCSVSerializer implements EventSerializer {

    private final static Logger logger = LoggerFactory.getLogger(FlumeEventCSVSerializer.class);

    // dynamic values from flume conf file
    public static final String FORMAT = "format";
    public static final String REGEX = "regex";
    public static final String REGEX_ORDER = "regexorder";

    // Default values
    private final String DEFAULT_FORMAT = "CSV";
    private final String DEFAULT_REGEX = "(.*)";
    private final String DEFAULT_ORDER = "1";

    private final String format;
    private final Pattern regex;
    private final String[] regexOrder;
    private final OutputStream out;
    private Map<Integer, ByteBuffer > orderIndexer;

    public FlumeEventCSVSerializer(OutputStream out, Context context) {
        this.format = context.getString(FORMAT, DEFAULT_FORMAT);
        this.regex = Pattern.compile(context.getString(REGEX, DEFAULT_REGEX));
        this.regexOrder = context.getString(REGEX_ORDER, DEFAULT_ORDER).split(" ");
        this.out = out;
        this.orderIndexer = new HashMap<Integer, ByteBuffer>();
    }

    @Override
    public void afterCreate() throws IOException {

    }

    @Override
    public void afterReopen() throws IOException {

    }

    @Override
    public void write(Event event) throws IOException {

        Matcher matcher = regex.matcher(new String(event.getBody(), Charsets.UTF_8));
        if (matcher.find()) {

            int groupIndex = 0;
            int totalGroups = matcher.groupCount();
            for (int i = 0, count = totalGroups; i < count; i++) {
                groupIndex = i + 1;
                orderIndexer.put(Integer.valueOf(regexOrder[i]), ByteBuffer.wrap(matcher.group(groupIndex).getBytes()));
            }

            writeEvnt1(event);

        } else {
            logger.warn("Error in the event processing : " + event.getBody().toString());
        }
    }

    private void writeEvnt1(Event event) throws IOException {

        Iterator t = orderIndexer.keySet().iterator();

        writes(orderIndexer.get(t.next()).array(), ';');
        writes(orderIndexer.get(t.next()).array(), ';');
        writes(orderIndexer.get(t.next()).array(), ';');
        writes(orderIndexer.get(t.next()).array(), ';');
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
        writes(orderIndexer.get(t.next()).array(), ';');
        writes(orderIndexer.get(t.next()).array(), ';');
        writes(event.getHeaders().get(TimestampInterceptor.Constants.TIMESTAMP).getBytes(), '\n');
    }

    private void writes(byte[] b, char pv) throws IOException {
        out.write(b);
        out.write(pv);
    }

    private void previousWrite(Event event) throws IOException {

        // write out the columns of the table
        for(Integer key : orderIndexer.keySet()) {
            out.write(orderIndexer.get(key).array());
            out.write(';');
        }
        out.write(event.getHeaders().get(TimestampInterceptor.Constants.TIMESTAMP).getBytes());
        out.write('\n');
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
}
