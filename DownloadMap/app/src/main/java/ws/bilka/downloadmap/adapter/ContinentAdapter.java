package ws.bilka.downloadmap.adapter;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ws.bilka.downloadmap.R;
import ws.bilka.downloadmap.model.Continent;

public class ContinentAdapter extends RecyclerView.Adapter<ContinentAdapter.ViewHolder> {
    private List<Continent> continents;

    public ContinentAdapter(List<Continent> continents) {
        this.continents = continents;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.world_regions_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Continent continent = continents.get(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.name.setText(Html.fromHtml(continent.getName(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.name.setText(Html.fromHtml(continent.getName()));
        }
    }

    @Override
    public int getItemCount() {
        if (continents == null)
            return 0;
        return continents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);

        }
    }
}
