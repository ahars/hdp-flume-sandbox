package org.apache.flume.serialization;

import com.google.common.base.Charsets;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Flume Serializer for events from Spideo
 */
public class FlumeEventCSVSerializer implements EventSerializer {

    private final static Logger logger = LoggerFactory.getLogger(FlumeEventCSVSerializer.class);

    // dynamic values from flume conf file
    public static final String REGEX = "regex";
    public static final String PATHERROR = "pathError";
    public static final String FILEERROR = "fileError";

    // Default values
    private final String DEFAULT_REGEX = "(.*)";
    private final String DEFAULT_PATHERROR = "/";
    private final String DEFAULT_FILEERROR = "error.csv";

    private final String fileError;
    private final String pathError;
    private final Pattern regex;
    private final Pattern default_regex;
    private final OutputStream out;
    private Matcher matcher;
    private ByteBuffer[] input1;
    private Set<String> input2;
    private Map<String, ByteBuffer > result;
    private final SimpleDateFormat ymd;
    private final SimpleDateFormat ym;

    /**
     * Constructor of the FlumeEventCSVSerializer
     * @param out output stream
     * @param context flume context given
     */
    public FlumeEventCSVSerializer(OutputStream out, Context context) {
        this.regex = Pattern.compile(context.getString(REGEX, DEFAULT_REGEX));
        this.default_regex = Pattern.compile(DEFAULT_REGEX);
        this.out = out;
        this.input2 = new LinkedHashSet<String>();
        this.result = new HashMap<String, ByteBuffer>();
        this.fileError = context.getString(FILEERROR, DEFAULT_FILEERROR);
        this.pathError = context.getString(PATHERROR, DEFAULT_PATHERROR);
        this.ymd = new SimpleDateFormat("yyyyMMdd");
        this.ym = new SimpleDateFormat("yyyyMM");
    }

    /**
     * Instruction to execute after a creation of the Serializer
     * @throws IOException
     */
    @Override
    public void afterCreate() throws IOException { }

    /**
     * Instructions to execute after a reopening
     * @throws IOException
     */
    @Override
    public void afterReopen() throws IOException { }

    /**
     * Write the different elements of the log in the output strem
     * @param event event to process
     * @throws IOException
     */
    @Override
    public void write(Event event) throws IOException {

        result.clear();
        matcher = regex.matcher(new String(event.getBody(), Charsets.UTF_8));

        if (matcher.find()) {

            /* Process of typical logs */
            alimOrderIndexer(matcher);

            result.put("cat_1", ByteBuffer.wrap(converterDateTime(new String(input1[0].array(), "UTF-8")).getBytes()));
            result.put("cat_2", ByteBuffer.wrap(converterDate(new String(input1[1].array(), "UTF-8")).getBytes()));
            result.put("cat_4", ByteBuffer.wrap(input1[2].array()));

            if (!Arrays.equals(input1[4].array(), "-".getBytes()))
                result.put("cat_20", ByteBuffer.wrap(input1[4].array()));
            if (!Arrays.equals(input1[5].array(), "-".getBytes()))
                result.put("cat_21", ByteBuffer.wrap(input1[5].array()));

            input2 = new LinkedHashSet(Arrays.asList(new String(input1[3].array(), "UTF-8").split("\\?|&|/")));

            Iterator it = input2.iterator();
            String tmp = (String) it.next();
            if (!tmp.equals("users"))
                result.put("cat_3", ByteBuffer.wrap(tmp.getBytes()));

            it = input2.iterator();

            while(it.hasNext()) {
                tmp = (String) it.next();

                if (tmp.equals("users")) {
                    tmp = (String) it.next();
                    String[] user = tmp.split(":|%");
                    result.put("cat_5", ByteBuffer.wrap(user[1].getBytes()));
                    result.put("cat_6", ByteBuffer.wrap(user[2].getBytes()));
                    result.put("cat_3", ByteBuffer.wrap(((String) it.next()).getBytes()));

                } else if (tmp.contains("=")) {
                    String[] kv = tmp.split("=");

                    if (kv[0].equals("limit"))
                        result.put("cat_13", ByteBuffer.wrap(kv[1].getBytes()));

                    else if (kv[0].equals("catalogs"))
                        result.put("cat_8", ByteBuffer.wrap(kv[1].getBytes()));

                    else if (kv[0].equals("details"))
                        result.put("cat_9", ByteBuffer.wrap(kv[1].getBytes()));

                    else if (kv[0].equals("rating"))
                        result.put("cat_19", ByteBuffer.wrap(kv[1].getBytes()));

                    else if (kv[0].equals("previous"))
                        result.put("cat_16", ByteBuffer.wrap(kv[1].getBytes()));

                    else if (kv[0].equals("lists"))
                        result.put("cat_15", ByteBuffer.wrap(kv[1].replace("%", ",").getBytes()));

                    else if (kv[0].equals("universe"))
                        result.put("cat_10", ByteBuffer.wrap(kv[1].getBytes()));

                    else if (kv[0].equals("media")) {
                        kv[1] = kv[1].replace("<", "");
                        kv[1] = kv[1].replace(">", "");
                        result.put("cat_7", ByteBuffer.wrap(kv[1].getBytes()));

                    } else if (kv[0].equals("user")) {
                        String[] user = kv[1].split(":|%");
                        result.put("cat_5", ByteBuffer.wrap(user[1].getBytes()));
                        result.put("cat_6", ByteBuffer.wrap(user[2].getBytes()));
                    }

                } else if (tmp.equals("userId"))
                    result.put("cat_5", ByteBuffer.wrap(((String) it.next()).getBytes()));

                else if (tmp.equals("start"))
                    result.put("cat_11", ByteBuffer.wrap(((String) it.next()).getBytes()));

                else if (tmp.equals("end"))
                    result.put("cat_12", ByteBuffer.wrap(((String) it.next()).getBytes()));

                else if (tmp.equals("flat"))
                    result.put("cat_14", ByteBuffer.wrap(((String) it.next()).getBytes()));

                else if (tmp.contains("_movie_"))
                    result.put("cat_17", ByteBuffer.wrap(tmp.getBytes()));

                else if (tmp.contains("_cat_"))
                    result.put("cat_18", ByteBuffer.wrap(tmp.getBytes()));

            }

            // Write the results to the output stream
            writeResult();

        } else {

            /* Reject of atypical logs */
            matcher = default_regex.matcher(new String(event.getBody(), Charsets.UTF_8));

            if (matcher.find()) {

                String error = pathError + fileError;
                error = error.replace("yyyyMMdd", ymd.format(new java.util.Date(System.currentTimeMillis())));
                error = error.replace("yyyyMM", ym.format(new java.util.Date(System.currentTimeMillis())));

                // Store them in HDFS
                PrintWriter file = new PrintWriter(new BufferedWriter(new FileWriter(error, true)));


                alimOrderIndexer(matcher);

                file.println(new String(input1[0].array(), "UTF-8"));
                file.close();

            } else {
                logger.warn("ERROR on parsing log : " + new String(event.getBody(), Charsets.UTF_8));
            }
        }
    }

    /**
     * Collect of the element of the given log
     * @param matcher the matcher used to get elements from the given log
     */
    private void alimOrderIndexer(Matcher matcher) {

        int groupIndex = 0;
        input1 = new ByteBuffer[matcher.groupCount()];

        for (int i = 0, count = matcher.groupCount(); i < count; i++) {
            groupIndex = i + 1;
            input1[i] = ByteBuffer.wrap(matcher.group(groupIndex).getBytes());
        }
    }

    /**
     * Write the values collected or NULL to the result with ponctuation
     * @throws IOException
     */
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
        writes(result.getOrDefault("cat_21", ByteBuffer.wrap("NULL".getBytes())).array(), ';');

        writes(ym.format(new java.util.Date(System.currentTimeMillis())).getBytes(), ';');
        writes(ymd.format(new java.util.Date(System.currentTimeMillis())).getBytes(), '\n');
    }

    /**
     * simplification for adding values to the result with ponctuation
     * @param b value to add to the result
     * @param pv char to add at the end of the first parameter like ';' or '\n'
     * @throws IOException
     */
    private void writes(byte[] b, char pv) throws IOException {

        out.write(b);
        out.write(pv);
    }

    /**
     * Instructions to execute when flushing
     * @throws IOException
     */
    @Override
    public void flush() throws IOException { }

    /**
     * Instructions to execute at the end of the Serializing
     * @throws IOException
     */
    @Override
    public void beforeClose() throws IOException { }

    /**
     * Converter of date from 'dd/MMM/yyyy' to 'yyyy-MM-dd'
     * @param st the date 'dd/MMM/yyyy' to convert
     * @return the date with the format 'yyyy-MM-dd'
     */
    private String converterDate(String st) {

        DateFormat df1 = new SimpleDateFormat("dd/MMM/yyyy");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return df2.format(df1.parse(st)).toString();
        } catch (ParseException e) {
            logger.error("Error converting date : ", e);
            return st;
        }
    }

    /**
     * Converter of date from 'dd/MMM/yyyy:HH:mm:ss ZZZ' to 'yyyy-MM-dd HH:mm:ss.ZZZ'
     * @param st the date 'dd/MMM/yyyy:HH:mm:ss ZZZ' to convert
     * @return the date with the format 'yyyy-MM-dd HH:mm:ss.ZZZ'
     */
    private String converterDateTime(String st) {

        String[] s = st.split(" ");
        SimpleDateFormat df1 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.");

        try {
            return df2.format(df1.parse(s[0])).toString().concat(s[1].substring(1));
        } catch (ParseException e) {
            logger.error("Error converting datetime : ", e);
            return st;
        }
    }

    /**
     * Support of the reopening
     * @return true
     */
    @Override
    public boolean supportsReopen() { return true; }

    /**
     * Static class to build the Serializer
     */
    public static class Builder implements EventSerializer.Builder {

        @Override
        public EventSerializer build(Context context, OutputStream out) {
            FlumeEventCSVSerializer s = new FlumeEventCSVSerializer(out, context);
            return s;
        }
    }
}
