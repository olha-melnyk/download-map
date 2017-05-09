package ws.bilka.downloadmap;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ws.bilka.downloadmap.model.Continent;
import ws.bilka.downloadmap.model.Region;

class RegionsParser {

    private static final String TAG = RegionsParser.class.getSimpleName();

    public static List<Continent> parse(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();

        int depth = 0;
        int prevDepth = 0;

        List<Continent> continents = new LinkedList<>();
        Continent currentContinent = null;
        Region currentRegion = null;

        while(eventType != XmlPullParser.END_DOCUMENT) {

            if(eventType == XmlPullParser.START_TAG) {
                depth++;

                String typeName = parser.getAttributeValue(null, "type");

                if (typeName != null && typeName.contains("continent")) {
                    String continentName = parser.getAttributeValue(null, "translate");
                    Log.i(TAG, "> [" + depth + "] continent: " + continentName);

                    Continent continent = new Continent(continentName);
                    currentContinent = continent;
                    continents.add(continent);

                } else {
                    String regionName = parser.getAttributeValue(null, "name");
                    Log.i(TAG, ">>> [" + depth + "] region: " + regionName);

                    if(currentContinent != null) {
                        Region region = new Region(regionName);

                        if(depth == 3) {
                            currentContinent.addRegion(region);
                        } else {
                            if(depth > prevDepth) {
                                currentRegion.addChild(region);

                            } else if(depth == prevDepth) {
                                currentRegion.getParent().addChild(region);
                            } else {
                                currentRegion.getParent().getParent().addChild(region);
                            }
                        }

                        currentRegion = region;
                    }
                }

            } else if(eventType == XmlPullParser.END_TAG) {
                depth--;
            }

            eventType = parser.next();
            prevDepth = depth;
        }

        for(Continent continent : continents) {

            Log.i(TAG, "continent: " + continents);
//            Log.i(TAG, "continent: " + continent.getName());

            for(Region region : continent.getRegions()) {
                Log.i(TAG, region.toString());
            }
        }

        return continents;
    }


}

