package com.duang.easyecard.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.duang.easyecard.GlobalData.MyApplication;
import com.duang.easyecard.Model.GridViewItem;
import com.duang.easyecard.R;
import com.duang.easyecard.Util.LogUtil;
import com.duang.easyecard.Util.ManagementGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagementFragment extends Fragment implements
        AdapterView.OnItemClickListener {

    private static final String TAG = "ManagementFragment";
    private StartActivitiesCallback startActivitiesCallback;
    // ItemImage图标封装为一个数组
    private int[] iconImage = {
            R.drawable.manage_basic_info,
            R.drawable.manage_trading_inquiry,
            R.drawable.manage_report_loss,
            R.drawable.manage_recharge,
            R.drawable.manage_net_charge,
            R.drawable.manage_change_password,
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof StartActivitiesCallback)) {
            throw new IllegalStateException("The host activity must implement the" +
                    " StartActivitiesCallback.");
        }
        // 把绑定的activity当成callback对象
        startActivitiesCallback = (StartActivitiesCallback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewFragment = inflater.inflate(R.layout.fragment_management, null);
        // 实例化控件
        GridView mGridView = (GridView) viewFragment.findViewById(R.id.manage_grid_view);
        ImageView mCampusImageView = (ImageView) viewFragment.findViewById(R.id.manage_campus_image_view);
        // 通过Glide设置mCampusImageView资源
        Glide
                .with(this)
                .load(R.drawable.main_campus_scenery)
                .into(mCampusImageView);
        // ItemText封装数组
        String[] iconText = new String[]{
                getResources().getString(R.string.basic_information),
                getResources().getString(R.string.trading_inquiry),
                getResources().getString(R.string.report_loss_card),
                getResources().getString(R.string.recharge),
                getResources().getString(R.string.net_charge),
                getResources().getString(R.string.change_password)
        };
        ManagementGridViewAdapter mAdapter = new ManagementGridViewAdapter(MyApplication.getContext(),
                getDataLists(iconImage, iconText),
                R.layout.item_manage_grid_view);
        // 配置适配器
        mGridView.setAdapter(mAdapter);
        // 设置监听器
        mGridView.setOnItemClickListener(this);
        return viewFragment;
    }

    // 获得数据List并返回
    public List<GridViewItem> getDataLists(int[] imageResources, String[] textArray) {
        List<GridViewItem> itemList = new ArrayList<>();
        if (imageResources.length == textArray.length) {
            GridViewItem gridViewItem;
            for (int i = 0; i < imageResources.length; i++) {
                gridViewItem = new GridViewItem();
                gridViewItem.setResourceId(imageResources[i]);
                gridViewItem.setString(textArray[i]);
                itemList.add(gridViewItem);
            }
        } else {
            LogUtil.e(TAG, "Error: Arrays' lengths don't match.");
        }
        return itemList;
    }

    // Item的点击事件,根据图片ID来确定点击对象
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (iconImage[position]) {
            case R.drawable.manage_basic_info:
                // 更新基本信息并跳转activity
                startActivitiesCallback.sendGETRequestToMobile(
                        MainActivity.CONSTANT_START_BASIC_INFORMATION);
                break;
            case R.drawable.manage_trading_inquiry:
                startActivitiesCallback.sendPrePostRequestForTradingInquiry();
                break;
            case R.drawable.manage_report_loss:
                // 调用接口，先检查用户是否已经挂失
                startActivitiesCallback.sendGETRequestToMobile(
                        MainActivity.CONSTANT_START_REPORT_LOSS);
                break;
            case R.drawable.manage_recharge:
                Toast.makeText(MyApplication.getContext(),
                        getString(R.string.charge_is_not_available),
                        Toast.LENGTH_SHORT).show();
                break;
            case R.drawable.manage_net_charge:
                // 缴网费
                Toast.makeText(MyApplication.getContext(),
                        getString(R.string.charge_is_not_available),
                        Toast.LENGTH_SHORT).show();
                break;
            case R.drawable.manage_change_password:
                // 修改查询密码
                startActivity(new Intent(this.getContext(), ManageChangePasswordActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        startActivitiesCallback = null;  // 移除前赋值为空
    }

    // StartManageBasicInformationCallback接口，为了在打开基本信息界面时及时更新信息
    public interface StartActivitiesCallback {
        void sendGETRequestToMobile(int openActivityFlag);

        void sendPrePostRequestForTradingInquiry();
    }
}
