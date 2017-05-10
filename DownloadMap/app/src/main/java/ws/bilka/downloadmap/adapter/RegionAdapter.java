package ws.bilka.downloadmap.adapter;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import ws.bilka.downloadmap.R;
import ws.bilka.downloadmap.RegionActivity;
import ws.bilka.downloadmap.model.Region;


public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.ViewHolder> {

    private static final String TAG = RegionAdapter.class.getSimpleName();
    private Region regions;

    private DownloadManager mgr=null;
    private long lastDownload=-1L;
    private Context context;

    private ProgressDialog progressBarDialog;

    public static final double SPACE_KB = 1024;
    public static final double SPACE_MB = 1024 * SPACE_KB;
    public static final double SPACE_GB = 1024 * SPACE_MB;
    public static final double SPACE_TB = 1024 * SPACE_GB;

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
                    startDownload(v);
                }
            });
        }
    }

    public RegionAdapter(Region regions, Context context) {
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
        Region region = regions.getChilds().get(position);
        String upperRegionName = region.getName().substring(0,1).toUpperCase() + region.getName().substring(1);
        holder.name.setText(upperRegionName);
        holder.dwnldStart.setVisibility(region.hasChilds() ? View.INVISIBLE : View.VISIBLE);
        progressBarDialog.setMessage(upperRegionName);
    }

    @Override
    public int getItemCount() {
        return regions.numOfChilds();
    }

    public void startDownload(View v) {
        final Uri uri=Uri.parse("http://download.osmand.net/download.php?standard=yes&file=Denmark_europe_2.obf.zip");
        Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .mkdirs();

        progressBarDialog.setTitle("Downloads");
        progressBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBarDialog.setProgress(0);

        mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        lastDownload=
                mgr.enqueue(new DownloadManager.Request(uri)
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "map")
                );


        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean downloading = true;

                while (downloading) {

                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(lastDownload);

                    Cursor cursor = mgr.query(q);
                    cursor.moveToFirst();
                    final int bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    final int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }

                    final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);

                    ((RegionActivity)context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            progressBarDialog.setProgressPercentFormat(null);
                            progressBarDialog.setProgressNumberFormat((bytes2String(bytes_downloaded)) + " from " + (bytes2String(bytes_total)));
                            progressBarDialog.setCancelable(false);
                            progressBarDialog.setProgress(dl_progress);

                            if (progressBarDialog.getProgress() == progressBarDialog.getMax()){
                                progressBarDialog.dismiss();
                            }
                        }
                    });
                    cursor.close();
                }

            }
        }).start();
        progressBarDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressBarDialog.dismiss();
            }
        });
        progressBarDialog.show();
        v.setEnabled(false);
    }

    public static String bytes2String(long sizeInBytes) {
        NumberFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);

        try {
            if ( sizeInBytes < SPACE_KB ) {
                return nf.format(sizeInBytes) + " Byte(s)";
            } else if ( sizeInBytes < SPACE_MB ) {
                return nf.format(sizeInBytes/SPACE_KB) + " KB";
            } else if ( sizeInBytes < SPACE_GB ) {
                return nf.format(sizeInBytes/SPACE_MB) + " MB";
            } else if ( sizeInBytes < SPACE_TB ) {
                return nf.format(sizeInBytes/SPACE_GB) + " GB";
            } else {
                return nf.format(sizeInBytes/SPACE_TB) + " TB";
            }
        } catch (Exception e) {
            return sizeInBytes + " MB";
        }
    }
}
