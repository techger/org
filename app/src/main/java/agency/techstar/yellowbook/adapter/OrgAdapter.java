package agency.techstar.yellowbook.adapter;

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

import agency.techstar.yellowbook.AppConfig;
import agency.techstar.yellowbook.R;
import agency.techstar.yellowbook.activity.OrgDetailActivity;
import agency.techstar.yellowbook.utils.ImageLoader;

/**
 * Created by Dolly on 8/1/2017.
 */

public class OrgAdapter extends BaseAdapter {

    final Context context;
    final JSONArray organizations;
    public ImageLoader imageLoader;

    private LayoutInflater inflater = null;

    public OrgAdapter(Context context, JSONArray organizations) {
        this.context = context;
        this.organizations = organizations;
        imageLoader = new ImageLoader(context);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return organizations.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return organizations.getJSONObject(position);
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
            vi = inflater.inflate(R.layout.organization_item, null);

        TextView pName = (TextView) vi.findViewById(R.id.textOrg);
        AppCompatImageView pImage  = (AppCompatImageView) vi.findViewById(R.id.imgOrg);


        try {
            pName.setText(organizations.getJSONObject(position).getString("org_name"));
            imageLoader.DisplayImage(AppConfig.AdminPageURL+"/"+organizations.getJSONObject(position).getString("org_image"), pImage);
            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent iDetail = new Intent(context, OrgDetailActivity.class);
                    try {
                        Toast.makeText(context, organizations.getJSONObject(position).getString("org_name"), Toast.LENGTH_SHORT).show();
                        iDetail.putExtra("org_id", organizations.getJSONObject(position).getString("org_id"));
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
