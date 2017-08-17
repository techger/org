package agency.techstar.child.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import agency.techstar.child.AppConfig;
import agency.techstar.child.adapter.OrgAdapter;
import agency.techstar.child.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class OrgFragment extends Fragment {

    private static final String TAG = OrgFragment.class.getSimpleName();
    private static View rootView;

    private GridView homeItemList;
    private ProgressBar prgLoading;
    private TextView txtAlert;
    private Handler mHandler;

    public static OrgFragment newInstance() {
        OrgFragment fragment = new OrgFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_organization, container, false);
        prgLoading = (ProgressBar) rootView.findViewById(R.id.newsLoading);
        homeItemList = (GridView) rootView.findViewById(R.id.newsItemList);
        txtAlert = (TextView) rootView.findViewById(R.id.newsTxtAlert);

        mHandler = new Handler(Looper.getMainLooper());

        homeItemList.setNumColumns(2);

        homeItemList.setVisibility(View.VISIBLE);

        getNews();

        return rootView;

    }

    public void getNews () {
        prgLoading.setVisibility(View.VISIBLE);
        String uri = AppConfig.OrgService;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(uri)
                .build();

        Log.e(TAG , request.toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Алдаа:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                mHandler.post(() -> {
                    try {
                        JSONObject prod = new JSONObject(String.valueOf("{org=" + res+"}"));
                        JSONArray prodItems = prod.getJSONArray("org");
                        Log.e(TAG, prodItems + "");
                        prgLoading.setVisibility(View.GONE);
                        homeItemList.setAdapter(new OrgAdapter(getActivity(), prodItems));
                    } catch (JSONException ex){
                        ex.printStackTrace();
                    }
                });
            }
        });
    }
}
