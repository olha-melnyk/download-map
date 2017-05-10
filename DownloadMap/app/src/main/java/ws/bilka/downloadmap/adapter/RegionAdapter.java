package ws.bilka.downloadmap.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import ws.bilka.downloadmap.R;
import ws.bilka.downloadmap.model.Region;


public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.ViewHolder> {

    private static final String TAG = RegionAdapter.class.getSimpleName();
    private List<Region> regions;

    private Context context;
    private ProgressDialog progressBarDialog;

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public ImageButton dwnldStart;
        public TextView name;

        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            name = (TextView) view.findViewById(R.id.region_name);
            progressBarDialog = new ProgressDialog(context);

            dwnldStart = (ImageButton)view.findViewById(R.id.download_btn);
            dwnldStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }
    }

    public RegionAdapter(List<Region> regions, Context context) {
        this.regions = regions;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.regions_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Region region = regions.get(position);
        holder.name.setText(region.getName());
        progressBarDialog.setMessage(region.getName());
    }

    @Override
    public int getItemCount() {
        return regions.size();
    }
}
