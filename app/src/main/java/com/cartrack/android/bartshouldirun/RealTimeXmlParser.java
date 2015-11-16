package com.cartrack.android.bartshouldirun;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sami Ebadi on 10/27/2015.
 */
public class RealTimeXmlParser {
    // We don't use namespaces
    private static final String ns = null;


    public Station parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readRoot(parser);
        } finally {
            in.close();
        }
    }

    private Station readRoot(XmlPullParser parser) throws XmlPullParserException, IOException {
        Station result = null;
        parser.require(XmlPullParser.START_TAG, ns, "root");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("station")) {
                result = readStation(parser);
            } else if(name.equals("message")) {
                result.XmlMessage = readTag(parser, "message");
            }else
            {
                skip(parser);
            }
        }
        return result;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    private Station readStation(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "station");
        Station result = new Station();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("etd")) {
                List<MainPageTrain> trains = readEtdTag(parser);
                if (result.Trains == null) {
                    result.Trains = new ArrayList<>();
                }
                for (MainPageTrain train : trains) {
                    result.Trains.add(train);
                }
            } else {
                skip(parser);
            }
        }
        return result;
    }

    private List<MainPageTrain> readEtdTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "etd");
        List<MainPageTrain> result = new ArrayList<>();
        String destination = null;
        String abbreviation = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals("destination")){
                destination = readTag(parser, "destination");
            }else if (name.equals("abbreviation")) {
                abbreviation = readTag(parser, "abbreviation");
            }else if (name.equals("estimate")) {
                MainPageTrain train = readEstimateTag(parser);
                train.XmlDestination = destination;
                train.XmlAbbr = abbreviation;
                train.IsEmpty = false;
                result.add(train);
            } else {
                skip(parser);
            }
        }
        return result;
    }

    private MainPageTrain readEstimateTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "estimate");
        MainPageTrain result = new MainPageTrain();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("minutes")) {
                result.setMinutes(readTag(parser, "minutes"));
            } else if (name.equals("platform")) {
                result.XmlPlatform = readTag(parser, "platform");
            } else if (name.equals("length")) {
                result.XmlLength = readTag(parser, "length");
            } else if (name.equals("hexcolor")) {
                result.setColor(readTag(parser, "hexcolor"));
            } else
            {
                skip(parser);
            }
        }
        return result;
    }

    // Processes title tags in the feed.
    private String readMinutes(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "minutes");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "minutes");
        return title;
    }

    private String readTag(XmlPullParser parser, String aTagName) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, aTagName);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, aTagName);
        return title;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("link")) {
            if (relType.equals("alternate")){
                link = parser.getAttributeValue(null, "href");
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // Processes summary tags in the feed.
    private String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "platform");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "platform");
        return summary;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
