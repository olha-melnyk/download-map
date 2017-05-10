package ws.bilka.downloadmap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import ws.bilka.downloadmap.adapter.RegionAdapter;
import ws.bilka.downloadmap.model.Region;

public class RegionActivity extends AppCompatActivity {

    private static final String TAG = RegionActivity.class.getSimpleName();
    RegionAdapter regionAdapter;
    RecyclerView recyclerView;
    Context context;
    Region continent;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        continent = (Region) getIntent().getSerializableExtra("continents");

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String upperRegionName = continent.getName().substring(0,1).toUpperCase() + continent.getName().substring(1);
        getSupportActionBar().setTitle(upperRegionName);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.region_recycle_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (continent.getChilds().get(position).hasChilds()) {
                            Intent intent = new Intent(RegionActivity.this, RegionActivity.class);
                            intent.putExtra("continents", continent.getChilds().get(position));
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegionActivity.this, "Download map!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        regionAdapter = new RegionAdapter(continent, getApplicationContext());
        recyclerView.setAdapter(regionAdapter);

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress);
        final TextView freeSpaceText = (TextView)findViewById(R.id.free_space);
        final float totalSpace = DeviceMemory.getInternalStorageSpace();
        final float occupiedSpace = DeviceMemory.getInternalUsedSpace();
        final float freeSpace = DeviceMemory.getInternalFreeSpace();
        final DecimalFormat outputFormat = new DecimalFormat("#.##");


        if (null != freeSpaceText) {
            freeSpaceText.setText(outputFormat.format(freeSpace) + " GB");
        }

        if (null != progressBar) {
            progressBar.setMax((int) totalSpace);
            progressBar.setProgress((int)occupiedSpace);
        }
    }
}
