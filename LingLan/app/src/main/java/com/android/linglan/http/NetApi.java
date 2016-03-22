package com.android.linglan.http;

import android.text.TextUtils;


import com.android.linglan.utils.AESCryptUtil;
import com.android.linglan.utils.LogUtil;
import com.android.linglan.utils.SharedPreferencesUtil;
import com.android.linglan.utils.ToastUtil;
import com.loopj.android.http.RequestParams;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by changyizhang on 12/15/14.
 */
public class NetApi {
    private static AESCryptUtil aesCryptUtil = new AESCryptUtil();
    public static String getAppKey() {
        return SharedPreferencesUtil.getString("appkey", null);
    }

    public static String getToken() {
        return SharedPreferencesUtil.getString("token", null);
    }

//    public static String getToken() {
//        return (SharedPreferencesUtil.getString("token", null) != null) ? // token 的值为null 吗？
//                SharedPreferencesUtil.getString("token", null) : // 不为空则为token 的值
//                SharedPreferencesUtil.getString("visittoken", null);// 为空则为 visittoken 的值
//    }

    /**
     * 获取AppKey
     * secret   密钥（必须）
     * device   设备类型ios、android（必须）
     * version   版本号（必须）
     * @param callback
     * @param mac   物理地址ID
     */
    public static void getAppKey(PasserbyClient.HttpCallback callback, String mac) {
        String url = String.format(Constants.URL_APP_KEY, aesCryptUtil.encrypt(Constants.SECRET), aesCryptUtil.encrypt("android"), aesCryptUtil.encrypt(Constants.VERSION), aesCryptUtil.encrypt(mac));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 用户获得验证码
     * @param callback
     * @param phone 手机号
     */
    public static void getUserCode(PasserbyClient.HttpCallback callback,String phone) {
        String url = String.format(Constants.URL_USER_CODE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(phone));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 用户登录
     * @param phone 手机号(post)（和username二选一）
     * @param code  验证码(post)
     * @param username  用户名(post)(和phone 二选一）
     * @param pwd   密码(post)
     * @param callback
     */
    public static void getRegisterCheckCode(PasserbyClient.HttpCallback callback, String phone, String code, String username, String pwd) {
        RequestParams params = new RequestParams();
        params.put("appkey", aesCryptUtil.encrypt(getAppKey()));
        if (!TextUtils.isEmpty(phone)) {
            ToastUtil.show(phone);
            LogUtil.e(phone);
            LogUtil.e(aesCryptUtil.encrypt(phone));
//            ToastUtil.show(aesCryptUtil.encrypt(phone));
            params.put("phone", aesCryptUtil.encrypt(phone));

            params.put("code", aesCryptUtil.encrypt(code));
        }
        if (!TextUtils.isEmpty(username)) {
            params.put("username", aesCryptUtil.encrypt(username));
            params.put("pwd", aesCryptUtil.encrypt(pwd));
        }
        PasserbyClient.post(Constants.USER_LOGIN, params, callback);
//        String url = String.format(Constants.USER_LOGIN) + code +"&phone=" + registerPhone;
//        PasserbyClient.get(url, callback);
    }

    /**
     * 用户退出登录
     * @param callback
     */
    public static void getUserExit(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_USER_EXIT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 首页推荐
     * @param callback
     * @param page  页码
     */
    public static void getHomeRecommend(PasserbyClient.HttpCallback callback, String page) {
        String url = String.format(Constants.URL_HOME_RECOMMEND, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 推荐文章
     * http://192.168.1.117:8082/Api/Index/recommend?page=1
     * */
    public static void getRecommendArticle( PasserbyClient.HttpCallback callback, String page) {
        String url = String.format(Constants.URL_RECOMMEND_ARTICLE) + aesCryptUtil.encrypt(getToken()) + "&page=" + aesCryptUtil.encrypt(page);
        PasserbyClient.get(url, callback);
    }

    /**
     * 全部文章分类
     * @param callback
     */
    public static void getAllArticleClassify( PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_ALL_ARTICLE_CLASSIFY, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 全部文章分类下的文章列表
     * @param callback
     * @param cateid  分类id
     * @param page  页码
     * @param order  排序方式('addtime按时间排序' ,'count_view按统计排序')
     */
    public static void getAllArticleClassifyList( PasserbyClient.HttpCallback callback, String cateid, String page, String order) {
        String url = String.format(Constants.URL_ALL_ARTICLE_CLASSIFY_List, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(cateid), aesCryptUtil.encrypt(page), aesCryptUtil.encrypt(order));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 文章详情
     * 192.168.1.117:8082/Api/Article/articleInfo?articleid=1769
     * */
    public static void getDetailsArticle( PasserbyClient.HttpCallback callback,String articleid) {
        String url = String.format(Constants.URL_DETAILS_ARTICLE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(articleid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 文章详情用户写（添加）笔记
     * @param callback
     * @param articleid 文章id（必须）
     * @param notescontent  笔记内容(必须)
     */
    public static void addDetailsArticleNote( PasserbyClient.HttpCallback callback,String articleid, String notescontent) {
        String url = String.format(Constants.URL_DETAILS_ARTICLE_NOTE_WRITE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(articleid), aesCryptUtil.encrypt(notescontent));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /* 未接 */
    /**
     * 文章详情用户预览（获取）笔记
     * @param callback
     * @param articleid 文章id（必须）
     */
    public static void getDetailsArticleNotePreview( PasserbyClient.HttpCallback callback,String articleid) {
        String url = String.format(Constants.URL_DETAILS_ARTICLE_NOTE_PREVIEW, aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(articleid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 文章点赞
     * @param callback
     * @param articleid 文章id（必须）
     * @param iscancel 是否取消点赞，0为点赞，1为取消
     */
    public static void getDetailsArticleLike( PasserbyClient.HttpCallback callback,String articleid, String iscancel) {
        String url = String.format(Constants.URL_DETAILS_ARTICLE_LIKE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(articleid), aesCryptUtil.encrypt(iscancel));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 文章收藏
     * @param callback
     * @param articleid 文章id（必须）
     * @param iscancel 是否取消收藏，0为收藏，1为取消
     */
    public static void getDetailsArticleCollect( PasserbyClient.HttpCallback callback,String articleid, String iscancel) {
        String url = String.format(Constants.URL_DETAILS_ARTICLE_COLLECT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(articleid), aesCryptUtil.encrypt(iscancel));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 全部专题
     * http://192.168.1.117:8082/Api/Special/speclist?page=1&order=addtime&cateid=null
     * */
    public static void getAllSubject( PasserbyClient.HttpCallback callback, String page, String addtime, String cateid) {
        String url = String.format(Constants.URL_ALL_SUBJECT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(page), aesCryptUtil.encrypt(addtime), aesCryptUtil.encrypt(cateid));
        PasserbyClient.get(url, callback);
        LogUtil.e("url=" + url);
    }

    /**
     * 全部专题分类列表
     * @param callback
     */
    public static void getAllSubjectClassifyList( PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_ALL_SUBJECT_CLASSIFY, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }


    /**
     * 推荐专题
     * http://192.168.1.117:8082/Api/Special/recspeclist
     * */
    public static void getRecommendSubject( PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_RECOMMEND_SUBJECT) + aesCryptUtil.encrypt(getToken());
        PasserbyClient.get(url, callback);
        LogUtil.e("url=" + url);
    }

    /**
     * 专题详情
     * 192.168.1.117:8082/Api/Special/specailcontent?specialid=1
     * */
    public static void getDetailsSubject( PasserbyClient.HttpCallback callback,String specialid) {
        String url = String.format(Constants.URL_DETAILS_SUBJECT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(specialid));
        PasserbyClient.get(url, callback);
        LogUtil.e("url=" + url);
    }

    /* 未接 */
    /**
     * 专题详情用户写（添加）笔记
     * @param callback
     * @param specialid 专题id（必须）
     * @param notescontent  笔记内容(必须)
     */
    public static void addDetailsSubjectNote( PasserbyClient.HttpCallback callback,String specialid, String notescontent) {
        String url = String.format(Constants.URL_DETAILS_SUBJECT_NOTE_WRITE) + aesCryptUtil.encrypt(getToken()) + "&specialid=" + aesCryptUtil.encrypt(specialid) +"&notescontent=" + aesCryptUtil.encrypt(notescontent);
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /* 未接 */
    /**
     * 专题详情用户预览（获取）笔记
     * @param callback
     * @param specialid 专题id（必须）
     */
    public static void getDetailsSubjectNotePreview( PasserbyClient.HttpCallback callback,String specialid) {
        String url = String.format(Constants.URL_DETAILS_SUBJECT_NOTE_PREVIEW) + aesCryptUtil.encrypt(getToken()) + "&specialid=" + aesCryptUtil.encrypt(specialid);
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 专题点赞
     * @param callback
     * @param specialid 文章id（必须）
     * @param iscancel 是否取消点赞，0为点赞，1为取消
     */
    public static void getDetailsSubjectLike( PasserbyClient.HttpCallback callback, String specialid, String iscancel) {
        String url = String.format(Constants.URL_DETAILS_SUBJECT_LIKE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(specialid), aesCryptUtil.encrypt(iscancel));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 专题收藏
     * @param callback
     * @param specialid 专题id（必须）
     * @param iscancel 是否取消收藏，0为收藏，1为取消
     */
    public static void getDetailsSubjectCollect(PasserbyClient.HttpCallback callback, String specialid, String iscancel) {
        String url = String.format(Constants.URL_DETAILS_SUBJECT_COLLECT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(specialid), aesCryptUtil.encrypt(iscancel));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取作者详情
     * @param callback
     * @param publisherid   作者（发布者）id(必须)
     */
    public static void getDetailsAuthor(PasserbyClient.HttpCallback callback, String publisherid) {
        String url = String.format(Constants.URL_DETAILS_AUTHOR, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(publisherid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 添加关注
     * @param callback
     * @param publisherid   发布者id(必须)
     */
    public static void addFollow(PasserbyClient.HttpCallback callback, String publisherid) {
        String url = String.format(Constants.URL_ADD_FOLLOW, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(publisherid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取我的关注列表
     * @param callback
     * @param order   排序方式('publishtime按发布时间排序')
     */
    public static void getFollowedList(PasserbyClient.HttpCallback callback, String order) {
        String url = String.format(Constants.URL_FOLLOWED_LIST, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(order));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取我的收藏-文章
     * @param callback
     */
    public static void getCollectArticle(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_COLLECT_ARTICLE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取我的收藏-专题
     * @param callback
     */
    public static void getCollectSubject(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_COLLECT_SUBJECT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取我的收藏-混合排序全部
     * @param callback
     */
    public static void getCollectAll(PasserbyClient.HttpCallback callback,String page) {
        String url = String.format(Constants.URL_COLLECT_ALL, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }


    /**
     * 意见反馈
     * @param callback
     * @param content 反馈的内容(必须)
     */
    public static void sendFeedback(PasserbyClient.HttpCallback callback, String content, String email) {
        String url = String.format(Constants.URL_SEND_FEEDBACK, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(content), aesCryptUtil.encrypt(email));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取关于我们信息
     * @param callback
     */
    public static void getAbout(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_ABOUT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     *  http://101.200.122.79/Api/Search/statistic?token=9706b8e2961c725c199ac85e4e541ba9
     * 获取全局热门搜索/历史搜索（统计）
     * @param callback
     */
    public static void getHistoryHotSearchKey(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_STATISTIC, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 关键字搜索全局（统计）
     * @param callback
     * @param key 关键字
     */
    public static void getSearchAll(PasserbyClient.HttpCallback callback,String key,String page) {
        String url = String.format(Constants.URL_SEARCH_ALL, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(key), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 清除全局历史搜索
     * @param callback
     */
    public static void clearHistory(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_CLEAR_HISTORY, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取专题热门搜索/历史搜索（统计）
     * @param callback
     */
    public static void getSubjectHistoryHotSearchKey(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_SUBJECT_STATISTIC, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 关键字搜索专题
     * @param callback
     * @param key 关键字
     */
    public static void getSearchSubject(PasserbyClient.HttpCallback callback,String key,String page) {
        String url = String.format(Constants.URL_SEARCH_SUBJECT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(key), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试
     * 获取文章热门搜索/历史搜索（统计）
     * @param callback
     */
    public static void getArticleHistoryHotSearchKey(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_ARTICLE_STATISTIC, aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 关键字搜索文章
     * @param callback
     * @param key 关键字
     */
    public static void getSearchArticle(PasserbyClient.HttpCallback callback,String key,String page) {
        String url = String.format(Constants.URL_SEARCH_ARTICLE, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(key), aesCryptUtil.encrypt(page));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试
     * 文章分类排序修改
     * @param callback
     * @param cateorder  排序修改后的分类  json类型的字符串
     */
    public static void geArticleOrderEdit(PasserbyClient.HttpCallback callback,String cateorder) {
        String url = String.format(Constants.URL_ARTICLE_ORDER_EDIT) + aesCryptUtil.encrypt(getToken()) + "&cateorder=" + aesCryptUtil.encrypt(cateorder);
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 获取地区
     * @param callback
     */
    public static void geArea(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_AREA) + aesCryptUtil.encrypt(getToken());
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 未调试
     * 发布者发布的文章列表
     * @param callback
     * @param publisherid
     */
    public static void getUserublisheridrtiitem(PasserbyClient.HttpCallback callback,String publisherid) {
        String url = String.format(Constants.URL_PUBLISHER_ARTIITEM, aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(publisherid));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 用户信息
     * @param callback
     */
    public static void getUserInfo(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_USER_INFO, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 用户信息修改
     * @param callback
     * @param alias 用户昵称(post)
     * @param name  真实姓名(post)
     * @param about  个人简介(post)
     * @param city   所在城市(post)
     * @param company 所在单位(post)
     * @param feature 个人特征
     */
    public static void getUserInfoEdit(PasserbyClient.HttpCallback callback, String alias, String name, String about, String city,String company,String feature) {
        RequestParams params = new RequestParams();
        params.put("appkey", aesCryptUtil.encrypt(getAppKey()));
        params.put("token", aesCryptUtil.encrypt(getToken()));
        params.put("alias", aesCryptUtil.encrypt(alias));
        params.put("name", aesCryptUtil.encrypt(name));
        params.put("about", aesCryptUtil.encrypt(about));
        params.put("city", aesCryptUtil.encrypt(city));
        params.put("company", aesCryptUtil.encrypt(company));
        params.put("feature", aesCryptUtil.encrypt(feature));
        PasserbyClient.post(Constants.URL_USER_INFO_EDIT + getToken(), params, callback);
    }

    /**
     * 上传用户头像
     * @param callback
     * @param avatar  排序修改后的分类  json类型的字符串
     */
    public static void getUserPhotoUpdate(PasserbyClient.HttpCallback callback,File avatar) {
        String url = String.format(Constants.URL_USER_PHOTO_UPLOAD) + aesCryptUtil.encrypt(getToken());
        RequestParams params = new RequestParams();
        params.put("token", aesCryptUtil.encrypt(getToken()));
        try {
            params.put("photo", avatar, "image/x-png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PasserbyClient.post(url, params, callback);
        LogUtil.e("url=" + url);
    }

    /**
     * 检查版本更新
     * @param callback
     * @param version
     */
    public static void getCheckUpdate(PasserbyClient.HttpCallback callback,String version) {
        String url = String.format(Constants.URL_CHECK_UPDATE, aesCryptUtil.encrypt("android"), aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()), aesCryptUtil.encrypt(version));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

    /**
     * 服务条款
     * @param callback
     */
    public static void getAppAgreement(PasserbyClient.HttpCallback callback) {
        String url = String.format(Constants.URL_APP_AGREEMENT, aesCryptUtil.encrypt(getAppKey()), aesCryptUtil.encrypt(getToken()));
        LogUtil.e("url=" + url);
        PasserbyClient.get(url, callback);
    }

}
