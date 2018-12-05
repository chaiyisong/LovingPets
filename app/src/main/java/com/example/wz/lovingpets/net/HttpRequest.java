package com.example.wz.lovingpets.net;

import com.example.wz.lovingpets.entity.Address;
import com.example.wz.lovingpets.entity.Collect;
import com.example.wz.lovingpets.entity.CollectGoodsInfo;
import com.example.wz.lovingpets.entity.CollectThemeInfo;
import com.example.wz.lovingpets.entity.GoodsDetailInfo;
import com.example.wz.lovingpets.entity.ListResponse;
import com.example.wz.lovingpets.entity.OrderInfo;
import com.example.wz.lovingpets.entity.PetInfo;
import com.example.wz.lovingpets.entity.ServiceComment;
import com.example.wz.lovingpets.entity.ServiceInfo;
import com.example.wz.lovingpets.entity.ServiceOrderInfo;
import com.example.wz.lovingpets.entity.ShoppingCartDetail;
import com.example.wz.lovingpets.entity.User;
import com.example.wz.lovingpets.utils.LoggingInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 使用retrofit2+Rxjava2+OkHttp3+自定义的网络日志记录器构建的网络访问类
 * LoggingInterceptor：带格式化json的自定义日志记录器，继承于okhttp3的日志记录器
 */
public class HttpRequest {
//    public static final String BASE_URL = "http://192.168.1.106:8080";//网络访问基地址
    public static final String BASE_URL = "http://192.168.43.234:8080";//网络访问基地址
//    public static final String BASE_URL = "http://193.112.48.16:8080";//网络访问基地址
    private static OkHttpClient.Builder builder = new OkHttpClient()
            .newBuilder()
            .addNetworkInterceptor(new LoggingInterceptor());
    private static ApiService apiService;

    /**
     * 获取单例
     * @return
     */
    public static ApiService getApiservice(){
        if(apiService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    public interface ApiService {
        // 用户登录
        @FormUrlEncoded
        @POST("/petserviceplatform/UserAction/login")
        Observable<ListResponse<User>> login(
                @Field("userName") String username,
                @Field("password") String password);
        // 用户注册
        @FormUrlEncoded
        @POST("/petserviceplatform/UserAction/register")
        Observable<ListResponse<User>> register(
                @Field("userName") String username,
                @Field("password") String password,
                @Field("phone") String phone);
        // 获取商品
        @FormUrlEncoded
        @POST("/petserviceplatform/GoodsAction/getByClassify")
        Observable<ListResponse<GoodsDetailInfo>> getGoods(
                @Field("classify") String classify,
                @Field("condition") String condition,
                @Field("collecterId") Integer collecterId
        );
        // 加入购物车
        @FormUrlEncoded
        @POST("/petserviceplatform/ShoppingCartAction/addGoodsToCart")
        Observable<ListResponse> addToCart(
                @Field("param") String param);

        // 获取收货地址
        @FormUrlEncoded
        @POST("/petserviceplatform/AddressAction/getAllAddress")
        Observable<ListResponse<Address>> getAllAddress(
                @Field("userId") int userId);

        // 获取默认收货地址
        @FormUrlEncoded
        @POST("/petserviceplatform/AddressAction/getCommonAddress")
        Observable<ListResponse<Address>> getCommonAddress(
                @Field("userId") int userId);

        // 获取订单
        @FormUrlEncoded
        @POST("/petserviceplatform/OrderAction/getOrdersByUserId")
        Observable<ListResponse<OrderInfo>> getOrder(
                @Field("userId") int userId);

        // 删除订单
        @FormUrlEncoded
        @POST("/petserviceplatform/OrderAction/deleteOrder")
        Observable<ListResponse> delOrder(
                @Field("orderId") int orderId);

        // 收货、付款
        @FormUrlEncoded
        @POST("/petserviceplatform/OrderAction/changeOrderState")
        Observable<ListResponse> changeOrderState(
                @Field("id") int orderId,
                @Field("orderState") int orderState);

        // 修改、添加、收货地址
        @FormUrlEncoded
        @POST("/petserviceplatform/AddressAction/addAddress")
        Observable<ListResponse> manageAddress(
                @Field("id") int id,
                @Field("userId") int userId,
                @Field("receiver") String receiver,
                @Field("contact") String contact,
                @Field("province") String province,
                @Field("city") String city,
                @Field("fullAddress") String fullAddress,
                @Field("isCommonAddress") int isCommonAddress);

        //获取宠物
        @FormUrlEncoded
        @POST("/petserviceplatform/PetAction/getMyPet")
        Observable<ListResponse<PetInfo>> getPets(@Field("id")int id);

        //删除宠物
        @FormUrlEncoded
        @POST("/petserviceplatform/PetAction/delete")
        Observable<ListResponse> delete(@Field("petId")int id);

        //添加修改宠物一体接口，带id为修改，为0则添加
        @FormUrlEncoded
        @POST("/petserviceplatform/PetAction/managePet")
        Observable<ListResponse> managePet(@Field("id") int id,
                                             @Field("nickName")String nickName,
                                             @Field("familyId")int familyId,
                                             @Field("userId")int userId,
                                             @Field("classId")int classId,
                                             @Field("state")String state,
                                             @Field("sex")String sex,
                                             @Field("icon")String icon,
                                             @Field("bs")String bs);

        //获取七牛云上传凭证
        @FormUrlEncoded
        @POST("/petserviceplatform/UploadAction/getToken")
        Observable<String> getToken(@Field("path") String path);

//        获取购物车列表
        @FormUrlEncoded
        @POST("/petserviceplatform/ShoppingCartAction/getMyCart")
        Observable<ListResponse<ShoppingCartDetail>> getSC(@Field("shoppingCartId") Integer shoppingCartId);

        //购物车转换为订单
        @FormUrlEncoded
        @POST("/petserviceplatform/OrderAction/cartToOrder")
        Observable<ListResponse> cartToOrder(
                @Field("goodsList")String goodsList,
                @Field("userId") Integer userId,
                @Field("addressId") Integer addressId,
                @Field("state") Integer state);

        //商品直接购买转换为订单
        @FormUrlEncoded
        @POST("/petserviceplatform/OrderAction/goodsToOrder")
        Observable<ListResponse> goodsToOrder(
                @Field("goodsDetailId")Integer goodsDetailId,
                @Field("addressId") Integer addressId,
                @Field("userId") Integer userId,
                @Field("count") Integer count);

        //获取用户的商品收藏
        @FormUrlEncoded
        @POST("/petserviceplatform/CollectAction/getCollectByUserId")
        Observable<ListResponse<CollectGoodsInfo>>  getCollectGoods(@Field("userId")Integer userId
                , @Field("type") Integer type);

        //获取用户的主题收藏
        @FormUrlEncoded
        @POST("/petserviceplatform/CollectAction/getCollectByUserId")
        Observable<ListResponse<CollectThemeInfo>>  getCollectTheme(@Field("userId")Integer userId
                , @Field("type") Integer type);

        //收藏操作
        @FormUrlEncoded
        @POST("/petserviceplatform/CollectAction/collect")
        Observable<ListResponse>  collect(
                @Field("targetId")Integer targetId,
                @Field("userId")Integer userId,
                @Field("type") Integer type);

        //取消收藏
        @FormUrlEncoded
        @POST("/petserviceplatform/CollectAction/unCollect")
        Observable<ListResponse>  unCollect(
                @Field("targetId")Integer targetId,
                @Field("userId")Integer userId,
                @Field("type") Integer type);

        //获取服务
        @FormUrlEncoded
        @POST("/petserviceplatform/ServiceAction/getServiceByType")
        Observable<ListResponse<ServiceInfo>> getServer(
                @Field("type") Integer type,
                @Field("condition")String condition
        );

        //获取服务订单
        @FormUrlEncoded
        @POST("/petserviceplatform/ServiceOrderAction/getForUser")
        Observable<ListResponse<ServiceOrderInfo>> getServerOrder(
                @Field("userId")Integer userId
        );

        //取消服务订单
        @FormUrlEncoded
        @POST("/petserviceplatform/ServiceOrderAction/updateOrder")
        Observable<ListResponse> cancelServerOrder(
                @Field("id")Integer id,
                @Field("state")byte state
        );

        //延后服务订单
        @FormUrlEncoded
        @POST("/petserviceplatform/ServiceOrderAction/updateOrder")
        Observable<ListResponse> delayServerOrder(
                @Field("id")Integer id,
                @Field("serverDate")String serverDate
        );

        //支付服务订单
        @FormUrlEncoded
        @POST("/petserviceplatform/ServiceOrderAction/payServiceOrder")
        Observable<ListResponse> payServerOrder(
                @Field("serviceOrderId")Integer serviceOrderId
        );

        //删除服务订单
        @FormUrlEncoded
        @POST("/petserviceplatform/ServiceOrderAction/delServiceOrder")
        Observable<ListResponse> delServiceOrder(
                @Field("serviceOrderId")Integer serviceOrderId
        );

        //预定服务，生成订单
        @FormUrlEncoded
        @POST("/petserviceplatform/ServiceOrderAction/addOrder")
        Observable<ListResponse> addServiceOrder(
                @Field("param")String param
        );

        //评论服务，生成服务评论
        @FormUrlEncoded
        @POST("/petserviceplatform/ServiceOrderAction/comentServiceOrder")
        Observable<ListResponse> comentServiceOrder(
                @Field("param")String param
        );

    }

    public Observable<ListResponse<User>> login(String username, String password) {
        return apiService.login(username, password);
    }

    public Observable<ListResponse<User>> register(String username, String password,String phone) {
        return apiService.register(username, password,phone);
    }

    public Observable<ListResponse<GoodsDetailInfo>> getGoods(String classify,String condition,Integer collecterId){
        return apiService.getGoods(classify,condition,collecterId);
    }

    public Observable<ListResponse> addToCart(String param){
        return apiService.addToCart(param);
    }

    public Observable<ListResponse<Address>> getAllAddress(int userId){
        return apiService.getAllAddress(userId);
    }

    public Observable<ListResponse<Address>> getCommonAddress(int userId){
        return apiService.getCommonAddress(userId);
    }

    public Observable<ListResponse<OrderInfo>> getOrder(int userId){
        return apiService.getOrder(userId);
    }

    public Observable<ListResponse> delOrder(int orderId){
        return apiService.delOrder(orderId);
    }

    public Observable<ListResponse> changeOrderState(int orderId,int orderState){
        return apiService.changeOrderState(orderId,orderState);
    }

    public Observable<ListResponse> manageAddress( int id,int userId,String receiver, String contact,
                      String province,String city,String fullAddress,int isCommonAddress){
        return apiService.manageAddress(id,userId,receiver,contact,
                province,city,fullAddress,isCommonAddress);
    }
    
    public Observable<ListResponse<PetInfo>> getPets(int id){
        return apiService.getPets(id);
    }

    public Observable<ListResponse> deletePet(int id){
        return apiService.delete(id);
    }

    public Observable<ListResponse> update(int id,String nickName,int familyId,int userId,
                                           int classId,String state,String sex,String icon,String bs){
        return apiService.managePet(id,nickName,familyId,userId,
        classId,state,sex,icon,bs);
    }

    public Observable<String> getToken(String path){
        return apiService.getToken(path);
    }

    public Observable<ListResponse<ShoppingCartDetail>> getSC(Integer SCId){
        return apiService.getSC(SCId);
    }

    public Observable<ListResponse> cartToOrder(String goodsList,Integer userId,Integer state,Integer addressId){
        return apiService.cartToOrder(goodsList,userId,state,addressId);
    }

    public Observable<ListResponse> goodsToOrder(Integer goodsDetailId,Integer userId,Integer count,Integer addressId){
        return apiService.goodsToOrder(goodsDetailId,userId,count,addressId);
    }

    public Observable<ListResponse<CollectGoodsInfo>>  getCollectGoods(Integer userId){
        return apiService.getCollectGoods(userId,0);
    }

    public Observable<ListResponse<CollectThemeInfo>>  getCollectTheme(Integer userId){
        return apiService.getCollectTheme(userId,1);
    }

    public Observable<ListResponse>  collect(Integer targetId,Integer userId,Integer type){
        return apiService.collect(targetId,userId,type);
    }

    public Observable<ListResponse>  unCollect(Integer targetId,Integer userId,Integer type){
        return apiService.unCollect(targetId,userId,type);
    }

    public Observable<ListResponse<ServiceInfo>>  getServer(Integer type,String condition){
        return apiService.getServer(type,condition);
    }

    public Observable<ListResponse<ServiceOrderInfo>>  getServerOrder(Integer userId){
        return apiService.getServerOrder(userId);
    }

    public Observable<ListResponse>  cancelServerOrder(Integer id,byte state){
        return apiService.cancelServerOrder(id,state);
    }

    public Observable<ListResponse>  delayServerOrder(Integer id,String date){
        return apiService.delayServerOrder(id,date);
    }

    public Observable<ListResponse>  payServerOrder(Integer serviceOrderId){
        return apiService.payServerOrder(serviceOrderId);
    }

    public Observable<ListResponse>  delServerOrder(Integer serviceOrderId){
        return apiService.delServiceOrder(serviceOrderId);
    }

    public Observable<ListResponse>  comentServiceOrder(String param){
        return apiService.comentServiceOrder(param);
    }

}
