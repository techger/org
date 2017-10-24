package agency.techstar.org.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import agency.techstar.org.AppConfig;
import agency.techstar.org.R;
import agency.techstar.org.activity.ProjectDetaikActivity;
import agency.techstar.org.utils.ImageLoader;

public class ProjectAdapter extends BaseAdapter{

    final Context context;
    final JSONArray projects;
    public ImageLoader imageLoader;


    LayoutInflater inflater = null;
    public ProjectAdapter(Context context, JSONArray projects) {
        this.context = context;
        this.projects = projects;
        imageLoader = new ImageLoader(context);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return projects.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return projects.getJSONObject(position);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(vi == null)
            vi = inflater.inflate(R.layout.project_item, null);

        TextView pName = (TextView) vi.findViewById(R.id.textViewG);
        AppCompatImageView pImage  = (AppCompatImageView) vi.findViewById(R.id.imageViewG);


        try {
            pName.setText(projects.getJSONObject(position).getString("project_name"));
            imageLoader.DisplayImage(AppConfig.AdminPageURL+"/"+projects.getJSONObject(position).getString("project_image"), pImage);
            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent iDetail = new Intent(context, ProjectDetaikActivity.class);
                    try {
                        Toast.makeText(context, projects.getJSONObject(position).getString("project_name"), Toast.LENGTH_SHORT).show();
                        iDetail.putExtra("project_id", projects.getJSONObject(position).getString("project_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    context.startActivity(iDetail);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR", e.getMessage());
        }
        return vi;
    }

}