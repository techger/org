package agency.techstar.yellowbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Dolly on 8/1/2017.
 */

public class ProjectAdapter extends BaseAdapter {

    final Context context;
    final JSONArray products;
    public ImageLoader imageLoader;

    private LayoutInflater inflater = null;

    public ProjectAdapter(Context context, JSONArray products) {
        this.context = context;
        this.products = products;
        imageLoader = new ImageLoader(context);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return products.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return products.getJSONObject(position);
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
            vi = inflater.inflate(R.layout.grid_item, null);

        TextView pName = (TextView) vi.findViewById(R.id.textViewG);
        ImageView pImage  = (ImageView) vi.findViewById(R.id.imageViewG);

        try {
            pName.setText(products.getJSONObject(position).getString("name"));
            imageLoader.DisplayImage(AppConfig.AdminPageURL+"/uploads/product_photos/"+products.getJSONObject(position).getString("folder"), pImage);
            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent iDetail = new Intent(context, BaiguullagaActivity.class);
                    try {
                        iDetail.putExtra("product_id", products.getJSONObject(position).getString("id"));
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
