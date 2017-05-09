package ws.bilka.downloadmap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ws.bilka.downloadmap.model.Continent;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    RecyclerView recyclerView;
    List<Continent> continents;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        continents = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            try {
                parser.setInput(getAssets().open("regions.xml"), "utf-8");
                continents = RegionsParser.parse(parser);
                ContinentAdapter adapter = new ContinentAdapter(continents);
                recyclerView.setAdapter(adapter);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
            // TODO add alert if something went wrong
        }

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress);
        final TextView freeSpaceText = (TextView)findViewById(R.id.free_space);
        final float totalSpace = DeviceMemory.getInternalStorageSpace();
        final float occupiedSpace = DeviceMemory.getInternalUsedSpace();
        final float freeSpace = DeviceMemory.getInternalFreeSpace();
        final DecimalFormat outputFormat = new DecimalFormat("#.##");


        if (null != freeSpaceText) {
            freeSpaceText.setText(outputFormat.format(freeSpace) + " MB");
        }

        if (null != progressBar) {
            progressBar.setMax((int) totalSpace);
            progressBar.setProgress((int)occupiedSpace);
        }
    }
}
