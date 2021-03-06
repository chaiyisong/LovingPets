package com.example.wz.lovingpets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wz.lovingpets.R;
import com.example.wz.lovingpets.adapter.ShopCartAdapter;
import com.example.wz.lovingpets.base.BaseFragmentActivity;
import com.example.wz.lovingpets.common.ObservableDecorator;
import com.example.wz.lovingpets.entity.ListResponse;
import com.example.wz.lovingpets.entity.ShoppingCartDetail;
import com.example.wz.lovingpets.net.HttpRequest;
import com.example.wz.lovingpets.utils.DecimalUtil;
import com.example.wz.lovingpets.widget.AddToCartDialog;
import com.example.wz.lovingpets.widget.ConfirmDialog;
import com.example.wz.lovingpets.widget.ConfirmDialogBuilder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;

import static com.example.wz.lovingpets.activity.MyApp.getContext;

public class ShoppingCartActivity extends BaseFragmentActivity {

    private RecyclerView rv;
    private TextView tv_total,tv_title;
    private CheckBox cb_all;
    private ImageView iv_back;
    private Button bt_pay;
    private String mParam1,mParam2;
    private ShopCartAdapter adapter;
    private ShopCartAdapter.CkeckBoxListener cbl;
    public static final String TEXT_TITLE = "content";
    private HttpRequest.ApiService api = HttpRequest.getApiservice();
    private List<ShoppingCartDetail> list_shopcart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        findViews();
        initData();
        getSC();
    }

    @Override
    protected void findViews() {
        rv = findViewById(R.id.shopcart_rv);
        tv_total = findViewById(R.id.shopcart_tv_total);
        cb_all = findViewById(R.id.shopcart_cb_all);
        bt_pay = findViewById(R.id.shopcart_bt_pay);
        tv_title = findViewById(R.id.titlebar_tv_title);
        iv_back = findViewById(R.id.titlebar_iv_left);
    }

    @Override
    protected void initData() {
        tv_title.setText("我的购物车");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cbl = new ShopCartAdapter.CkeckBoxListener() {
            @Override
            public void onCkeckboxSwitch(String amount) {
                tv_total.setText("￥" + amount);
            }
        };
        cb_all.setChecked(false);
        cb_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_all.isChecked()){
                    selectAll(list_shopcart,cb_all.isChecked());
                }else{
                    tv_total.setText("0.0");
                    adapter.setAmount("0.0");
                    unSelectAll(list_shopcart,cb_all.isChecked());
                }
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new ShopCartAdapter(list_shopcart,getContext(),cb_all);
        adapter.setCbl(cbl);
        rv.setAdapter(adapter);
        bt_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payForCart();
            }
        });
    }
    private void payForCart() {
        final List<ShoppingCartDetail> items = adapter.getSeletedItem();
        if(items.size() == 0){
            showToast("你还没有选择结算的商品哦");
        }else{
            Intent intent = new Intent(getContext(), ConfirmOrderActivity.class);
            intent.putExtra("data",new Gson().toJson(items));
            intent.putExtra("type",0);
            startActivity(intent);
        }
    }


    private void unSelectAll(List<ShoppingCartDetail> list,boolean b) {
        for(ShoppingCartDetail s :list){
            s.setSelected(b);
        }
    }

    private void selectAll(List<ShoppingCartDetail> list,boolean b) {
        String total = "0.0";
        for(ShoppingCartDetail s :list){
            total = DecimalUtil.add(total,String.valueOf(s.getTotal()));
            s.setSelected(b);
        }
        tv_total.setText(total);
        adapter.setAmount(total);
    }

    private void getSC(){
        Observable<ListResponse<ShoppingCartDetail>> observable = api.getSC(MyApp.getInstance().getUser().getShoppingcartId());
        ObservableDecorator.decorate(observable, new ObservableDecorator.SuccessCall<ListResponse<ShoppingCartDetail>>() {
            @Override
            public void onSuccess(ListResponse<ShoppingCartDetail> listResponse) {
                loadDataSuccess(listResponse);
            }
        });
    }

    private void loadDataSuccess(ListResponse<ShoppingCartDetail> listResponse) {
        if(adapter == null){
            adapter = new ShopCartAdapter(listResponse.getRows(),getApplicationContext(),cb_all);
            adapter.setCbl(cbl);
            rv.setAdapter(adapter);
        }else{
            list_shopcart.clear();
            list_shopcart.addAll(listResponse.getRows());
            adapter.notifyDataSetChanged();
        }
    }

}
