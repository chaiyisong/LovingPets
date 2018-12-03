package com.example.wz.lovingpets.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wz.lovingpets.R;
import com.example.wz.lovingpets.entity.ShoppingCartDetail;
import com.example.wz.lovingpets.utils.DecimalUtil;
import com.example.wz.lovingpets.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.SCHolder> {
    private Context context;
    private CkeckBoxListener cbl;
    private CheckBox cb_all;
    private boolean isShow = true;
    private String amount = "0.0";
    private LayoutInflater inflater;
    private List<ShoppingCartDetail> list;

    public void setCbl(CkeckBoxListener cbl) {
        this.cbl = cbl;
    }
    public void setAmount(String amount){this.amount = amount;}
    public interface CkeckBoxListener{
        void onCkeckboxSwitch(String amount);
    }
    public ShopCartAdapter(List<ShoppingCartDetail> list, Context context,CheckBox cb) {
        this.cb_all = cb;
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public ShopCartAdapter(List<ShoppingCartDetail> list, Context context,boolean isShow) {
        this.list = list;
        this.context = context;
        this.isShow = isShow;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SCHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SCHolder(inflater.inflate(R.layout.item_shopcart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SCHolder holder, int position) {
        final ShoppingCartDetail data = list.get(position);
        holder.tv_name.setText(data.getGoodsName());
        holder.np.setText("x"+data.getCount());
        holder.tv_price.setText(data.getPrice()+"");
        holder.tv_count.setText(data.getTotal()+"");
        holder.tv_shopName.setText(data.getShopName());
        if(isShow){
            holder.cb.setChecked(data.isSelected());
            holder.cb.setOnClickListener(new CheckBox.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(holder.cb.isChecked()){
                        amount = DecimalUtil.add(amount,String.valueOf(data.getTotal()));
                        cbl.onCkeckboxSwitch(amount);
                        data.setSelected(true);
                    }else{
                        cb_all.setChecked(false);
                        amount = DecimalUtil.subtract(amount,String.valueOf(data.getTotal()));
                        cbl.onCkeckboxSwitch(amount);
                        data.setSelected(false);
                    }
                }
            });
        }else{
            holder.cb.setVisibility(View.GONE);
        }
        ImageUtil.loadNetImage(holder.iv,data.getImage());
    }

    public List<ShoppingCartDetail> getSeletedItem(){
        List<ShoppingCartDetail> result = new ArrayList<>();
        for (ShoppingCartDetail s :list){
            if(s.isSelected()){
                result.add(s);
            }
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SCHolder extends RecyclerView.ViewHolder{

        public CheckBox cb;
        public ImageView iv;
        public TextView np;
        public TextView tv_name,tv_price,tv_count,tv_shopName;
        public SCHolder(View view) {
            super(view);
            iv = view.findViewById(R.id.item_shopcart_iv);
            cb = view.findViewById(R.id.item_shopcart_cb);
            np = view.findViewById(R.id.item_shopcart_np);
            tv_name = view.findViewById(R.id.item_shopcart_goods_name);
            tv_price = view.findViewById(R.id.item_shopcart_price);
            tv_count = view.findViewById(R.id.item_shopcart_count);
            tv_shopName = view.findViewById(R.id.item_shopcart_shopName);
        }
    }
}
