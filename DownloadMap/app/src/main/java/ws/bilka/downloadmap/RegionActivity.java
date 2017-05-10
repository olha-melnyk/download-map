package ws.bilka.downloadmap;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
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

import ws.bilka.downloadmap.adapter.ContinentAdapter;
import ws.bilka.downloadmap.adapter.RegionAdapter;
import ws.bilka.downloadmap.model.Continent;
import ws.bilka.downloadmap.model.Region;

public class RegionActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    RegionAdapter regionAdapter;
    RecyclerView recyclerView;
    List<Region> regions;
    Context context;
    Continent continent;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        continent = (Continent) getIntent().getSerializableExtra("continents");
        Log.i(TAG, "Continent name: " + continent.getName());

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(continent.getName());

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        regions = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.region_recycle_view);
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

                regionAdapter = new RegionAdapter(continent.getRegions(), getApplicationContext());
                recyclerView.setAdapter(regionAdapter);


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
