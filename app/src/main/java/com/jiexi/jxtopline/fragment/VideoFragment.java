package com.jiexi.jxtopline.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.itheima.PullToRefreshView;
import com.jiexi.jxtopline.R;
import com.jiexi.jxtopline.adapter.VideoListAdapter;
import com.jiexi.jxtopline.bean.VideoBean;
import com.jiexi.jxtopline.utils.Constant;
import com.jiexi.jxtopline.utils.JsonParse;
import com.jiexi.jxtopline.view.WrapRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VideoFragment extends Fragment {
    private PullToRefreshView mPullToRefreshView;
    private WrapRecyclerView recycleView;
    public static final int REFRESH_DELAY = 1000;
    public static final int MSG_VIDEO_OK = 1; //获取数据
    private MHandler mHandler;                   //事件捕获
    private VideoListAdapter adapter;
    public VideoFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mHandler = new MHandler();
        initData();
        View view = initView(inflater, container);
        return view;
    }
    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        recycleView = (WrapRecyclerView) view.findViewById(R.id.recycler_view);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VideoListAdapter(getActivity());
        recycleView.setAdapter(adapter);
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.
                pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.
                OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        initData();
                    }
                }, REFRESH_DELAY);
            }
        });
        return view;
    }
    private void initData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Constant.WEB_SITE + Constant.
                REQUEST_VIDEO_URL).build();
        Call call = okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string();
                Message msg = new Message();
                msg.what = MSG_VIDEO_OK;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }
    /**
     * 事件捕获
     */
    class MHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_VIDEO_OK:
                    if (msg.obj != null) {
                        String vlResult = (String) msg.obj;
                        //使用Gson解析数据
                        List<VideoBean> videoList = JsonParse.getInstance().
                                getVideoList(vlResult);
                        adapter.setData(videoList);
                    }
                    break;
            }
        }
    }
}
