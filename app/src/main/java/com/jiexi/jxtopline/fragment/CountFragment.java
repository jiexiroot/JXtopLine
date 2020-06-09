package com.jiexi.jxtopline.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;

import com.jiexi.jxtopline.R;
import com.jiexi.jxtopline.activity.AndroidCountActivity;
import com.jiexi.jxtopline.activity.JavaCountActivity;
import com.jiexi.jxtopline.utils.BuilderManager;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CountFragment extends Fragment {
    private BoomMenuButton bmb;

    public CountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_count, container, false);
        bmb = (BoomMenuButton) view.findViewById(R.id.bmb);
        assert bmb != null;
        //设置点击圆形菜单后显示的多个按钮为圆形且带文本的
        bmb.setButtonEnum(ButtonEnum.TextInsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_9_1); //设置右下角圆形菜单中有9个圆形
        //设置点击右下角圆形菜单后显示的按钮为9个圆形Button
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_9_1);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            addBuilder();
        }
        return view;
    }

    private void addBuilder() {
        Log.d(TAG, "addBuilder: "+BuilderManager.getImageResource());
        bmb.addBuilder(new TextInsideCircleButton.Builder()
                .normalImageRes(BuilderManager.getImageResource())
                .normalTextRes(BuilderManager.getTextResource())
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        switch (index) {
                            case 0:   //跳转到Android统计详情界面
                                Intent android = new Intent(getActivity(), AndroidCountActivity.class);
                                startActivity(android);
                                break;
                            case 1:   //跳转到Java统计详情界面
                                Intent java = new Intent(getActivity(), JavaCountActivity.class);
                                startActivity(java);
                                break;
                        }
                    }
                }));
    }
}
