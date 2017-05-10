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

    public static List<Region> parse(XmlPullParser parser) throws XmlPullParserException, IOException {

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
                    Continent continent = new Continent(continentName);
                    currentContinent = continent;
                    continents.add(continent);
                } else {
                    String regionName = parser.getAttributeValue(null, "name");
                    Log.i(TAG, ">>> [" + depth + " prev:" + prevDepth + "] region: " + regionName);

                    if(currentContinent != null) {
                        Region region = null;
                        if(depth == 3) {
                            region = new Region(regionName);
                            currentContinent.addRegion(region);
                        } else {
                            if(depth > prevDepth) {
                                region = new Region(regionName);
                                currentRegion.addChild(region);

                            } else if(depth == prevDepth) {
                                Log.i(TAG, "WTF?");
                                region = new Region(regionName);
                                currentRegion.getParent().addChild(region);
                            } else {
                                region = new Region(regionName);
                                if(currentRegion.getParent() != null && currentRegion.getParent().getParent() != null)
                                    currentRegion.getParent().getParent().addChild(region);
                                else
                                    currentRegion.addChild(region);
                            }
                        }
                        currentRegion = region;
                    }
                }
                prevDepth = depth;

            } else if(eventType == XmlPullParser.END_TAG) {
                depth--;
            }
            eventType = parser.next();
        }

        List<Region> regions = new LinkedList<>();
        for(Continent continent : continents) {
            Region region = new Region(continent.getName());
            for(Region r : continent.getRegions()) {
                r.setParent(region);
                region.addChild(r);
            }
            regions.add(region);
        }
        return regions;
    }
}

