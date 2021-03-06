package com.example.wz.lovingpets.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wz.lovingpets.R;
import com.example.wz.lovingpets.activity.ApplyActivity;
import com.example.wz.lovingpets.activity.ManageAddressActivity;
import com.example.wz.lovingpets.activity.MyApp;
import com.example.wz.lovingpets.activity.MyCollectionActivity;
import com.example.wz.lovingpets.activity.MyPetsActivity;
import com.example.wz.lovingpets.activity.ServerOrderActivity;
import com.example.wz.lovingpets.activity.ShoppingCartActivity;
import com.example.wz.lovingpets.activity.UserInfoActivity;
import com.example.wz.lovingpets.base.BaseFragment;
import com.example.wz.lovingpets.common.BindEventBus;
import com.example.wz.lovingpets.common.Event;
import com.example.wz.lovingpets.common.EventCodes;
import com.example.wz.lovingpets.entity.User;
import com.example.wz.lovingpets.ui.login.LoginActivity;
import com.example.wz.lovingpets.ui.order.OrderActivity;
import com.example.wz.lovingpets.ui.register.RegisterActivity;
import com.example.wz.lovingpets.utils.ImageUtil;
import com.example.wz.lovingpets.utils.UserUtil;
import com.example.wz.lovingpets.widget.MyScrollView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@BindEventBus
public class MineFragment extends BaseFragment implements View.OnClickListener, MyScrollView.ScrollListener {
    private User user;
    private String mParam1;
    private String mParam2;
    private static int height;
    private MyScrollView scrollView;
    private RelativeLayout rl_title;
    private TextView tv_title, tv_login, tv_register;
    public static final String TEXT_TITLE = "content";
    private ImageView iv_setting, iv_banner, iv_devide,iv_head;
    private LinearLayout ll_pets_info, ll_my_address,ll_unpay,
            ll_unreceive,ll_evaluate,ll_all,ll_my_cart,ll_my_collection,
            ll_my_server,mine_ll_userInfo,ll_my_apply;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(TEXT_TITLE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        findViews(view);
        initDatas();
        initListeners();
        return view;
    }

    public static MineFragment newInstance(String param1) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(TEXT_TITLE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        resetUserData();
    }

    public void findViews(View view) {
        tv_title = view.findViewById(R.id.mine_tv_title);
        tv_login = view.findViewById(R.id.mine_tv_login);
        tv_register = view.findViewById(R.id.mine_tv_register);
        rl_title = view.findViewById(R.id.mine_rl_title);
        scrollView = view.findViewById(R.id.mine_scroll);
        iv_devide = view.findViewById(R.id.mine_iv_devide);
        iv_setting = view.findViewById(R.id.mine_iv_setting);
        iv_banner = view.findViewById(R.id.mine_background);
        iv_head = view.findViewById(R.id.mine_iv_head);
        ll_pets_info = view.findViewById(R.id.ll_pets_info);
        ll_my_address = view.findViewById(R.id.ll_my_address);
        ll_unpay = view.findViewById(R.id.mine_ll_unpay);
        ll_unreceive = view.findViewById(R.id.mine_ll_unreceive);
        ll_evaluate = view.findViewById(R.id.mine_ll_evaluate);
        ll_my_server = view.findViewById(R.id.ll_my_server);
        mine_ll_userInfo = view.findViewById(R.id.mine_ll_userInfo);
        ll_all = view.findViewById(R.id.mine_ll_all);
        ll_my_cart = view.findViewById(R.id.ll_my_cart);
        ll_my_apply = view.findViewById(R.id.ll_my_apply);
        ll_my_collection = view.findViewById(R.id.ll_my_collection);
    }

    public void initDatas() {
        user = new UserUtil(getContext()).getUser();
        if(user.getId() != 0){
            resetUserData();
            mine_ll_userInfo.setOnClickListener(this);
        }else{
            tv_register.setOnClickListener(this);
            tv_login.setOnClickListener(this);
        }
        iv_setting.setOnClickListener(this);
        ll_pets_info.setOnClickListener(this);
        ll_my_address.setOnClickListener(this);
        ll_my_server.setOnClickListener(this);
        ll_unpay.setOnClickListener(this);
        ll_unreceive.setOnClickListener(this);
        ll_evaluate.setOnClickListener(this);
        ll_all.setOnClickListener(this);
        ll_my_cart.setOnClickListener(this);
        ll_my_apply.setOnClickListener(this);
        ll_my_collection.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccess(Event<User> event) {
        if (event.getCode() == EventCodes.LOGIN_SUCCESS) {
            resetUserData();
        }
    }

    private void resetUserData() {
        user = new UserUtil(getActivity()).getUser();
        tv_register.setVisibility(View.GONE);
        iv_devide.setVisibility(View.GONE);
        tv_login.setText(user.getUserName());
        ImageUtil.loadRoundImage(iv_head,user.getIcon());
    }

    @Override
    public void onClick(View v) {
        Bundle b = new Bundle();
        switch (v.getId()) {
            case R.id.mine_iv_setting:
                Toast.makeText(getContext(), "setting clicked", Toast.LENGTH_LONG).show();
                break;
            case R.id.mine_background:
                Toast.makeText(getContext(), "banner clicked", Toast.LENGTH_LONG).show();
                break;
            case R.id.mine_tv_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.mine_tv_register:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                break;
            case R.id.ll_pets_info:
                startActivity(new Intent(getActivity(), MyPetsActivity.class));
                break;
            case R.id.ll_my_apply:
                if(user.getIdentity() == 2){
                    startActivity(new Intent(getActivity(), ApplyActivity.class));
                }else{
                    showLongToast("您已经是店家了！不能再次申请");
                }
                break;
            case R.id.ll_my_address:
                startActivity(new Intent(getActivity(), ManageAddressActivity.class));
                break;
            case R.id.mine_ll_unpay:
                b.putInt("orderState",2);
                intentWithData(OrderActivity.class,b);
                break;
            case R.id.mine_ll_unreceive:
                b.putInt("orderState",3);
                intentWithData(OrderActivity.class,b);
                break;
            case R.id.mine_ll_evaluate:
                b.putInt("orderState",4);
                intentWithData(OrderActivity.class,b);
                break;
            case R.id.mine_ll_all:
                b.putInt("orderState",0);
                intentWithData(OrderActivity.class,b);
                break;
            case R.id.ll_my_cart:
                startActivity(new Intent(getActivity(), ShoppingCartActivity.class));
                break;
            case R.id.ll_my_collection:
                startActivity(new Intent(getActivity(), MyCollectionActivity.class));
                break;
            case R.id.ll_my_server:
                startActivity(new Intent(getActivity(), ServerOrderActivity.class));
                break;
            case R.id.mine_ll_userInfo:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onScrollChanged(int x, int y, int oldx, int oldy, int computeVerticalScrollRange) {
        if (y <= 50) {   //设置标题的背景颜色
            tv_title.setTextColor(Color.argb((int) 0, 0, 0, 0));
            rl_title.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
        } else if (y > 50 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            //Log.i("Tag", "onScrollChanged: Y:"+y);
            float scale = (float) y / height;
            float alpha = (255 * scale);
            tv_title.setTextColor(Color.argb((int) alpha, 0, 0, 0));
            rl_title.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
        } else {    //滑动到banner下面设置普通颜色
            rl_title.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
        }
    }

    private void initListeners() {

        ViewTreeObserver vto = iv_banner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rl_title.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = iv_banner.getHeight();

                scrollView.setScrollViewListener(MineFragment.this);
            }
        });
    }
}
