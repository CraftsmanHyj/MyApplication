package com.hyj.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.hyj.demo.ActivityService.MessengerActivit;
import com.hyj.demo.a.HelloChild;
import com.hyj.demo.adapter.AdapterAcitivty;
import com.hyj.demo.adapter.recycler.MultiItemListViewActivity;
import com.hyj.demo.adapter.recycler.MultiItemRvActivity;
import com.hyj.demo.adapter.recycler.RecyclerAdapterActivity;
import com.hyj.demo.adapter.recycler.RecyclerViewActivity;
import com.hyj.demo.adapter.recycler.RvWidthHeaderActivity;
import com.hyj.demo.annotaionsframe.AnnotationsActivity_;
import com.hyj.demo.annotation.AnnotationActivity;
import com.hyj.demo.circleimageview.CircleImageActivity;
import com.hyj.demo.downservice.DownServiceActivity;
import com.hyj.demo.flowlayout.FlowLayoutActivity;
import com.hyj.demo.gobang.GobangActivity;
import com.hyj.demo.image_mt.ImageMain;
import com.hyj.demo.image_preview.ImagePreviewActivity;
import com.hyj.demo.imagecycle.ImageCycleViewActivity;
import com.hyj.demo.indicator.ViewPagerIndicatorActivity;
import com.hyj.demo.jigsaw.JigsawActivity;
import com.hyj.demo.largeImage.LargeImageViewActivity;
import com.hyj.demo.listviewindex.ListViewIndexActivity;
import com.hyj.demo.listviewrefresh.ListViewRfreshActivity;
import com.hyj.demo.lock.LockActivity;
import com.hyj.demo.lock.lockpattern.LockPatternActivity;
import com.hyj.demo.lock.lockpattern2.LockTestActivity;
import com.hyj.demo.lock.lockpattern3.Lock3Activity;
import com.hyj.demo.luckydial.LuckyDialActivity;
import com.hyj.demo.mainview.qq5_0.SlidingActivity;
import com.hyj.demo.mainview.tabfragment.FragmentTabActivity;
import com.hyj.demo.mainview.wechat.WeChatActivivty;
import com.hyj.demo.popup.PopupActivity;
import com.hyj.demo.porgress.ProgressBarActivity;
import com.hyj.demo.recyclerview.RecyclerActivity;
import com.hyj.demo.scratch.ScratchCardActivity;
import com.hyj.demo.six.SixGameActivity;
import com.hyj.demo.startmenu.StartMenu;
import com.hyj.demo.startmenu.StartMenu2;
import com.hyj.demo.title_bar.TitleBarActivity;
import com.hyj.lib.tools.LogUtils;
import com.hyj.lib.tools.ToastUtils;
import com.hyj.demo.tree.TreeActivity;
import com.hyj.demo.tuling.TulingActivity;
import com.hyj.demo.ui.DialogActivity;
import com.hyj.demo.ui.SquareActivity;
import com.hyj.demo.ui.TimerCountActivity;
import com.hyj.demo.viewpager.ViewPagerActivity;
import com.hyj.demo.viewpager.ViewPagerCustormerActivity;
import com.hyj.demo.wechat_imageUp.ImageLoaderActivity;
import com.hyj.demo.wechat_talk.WeChatTalkActivity;
import com.hyj.demo.wish.WishActivity;
import com.hyj.demo.zxing.ZxingActivity;
import com.hyj.demo.camera.CameraMainActivity;
import com.hyj.lib.tools.appinfo.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     APP主界面入口
 *     D:\ProgamFiles(green)\Android\Android Studio\bin
 * </pre>
 */
public class MainDemoActivity extends BaseActivity {
    /**
     * 猜歌游戏APP
     */
    private final String APP_MUSIC = "com.hyj.music";
    /**
     * 传递过去的bundle数据
     */
    public static final String DATA_BUNDLE = "dataBundle";

    private ListView lvItem;
    private MainDemoAdapter adapter;
    private List<ListItem> lItems = new ArrayList<ListItem>();

    private long exitTime;//APP退出标记

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_main);

        myInit();
        myTest();
    }

    private void myTest() {
        LogUtils.e("继承关系测试");
        new HelloChild();

        LogUtils.e("实例化对象测试");
        String s = new String("abc");
        String s1 = "abc";
        String s2 = new String("abc");

        LogUtils.i("s==s1：" + (s == s1));
        LogUtils.i("s==s2：" + (s == s2));
        LogUtils.i("s1==s2：" + (s1 == s2));
    }

    private void myInit() {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        lvItem = (ListView) findViewById(R.id.mainLv);
        adapter = new MainDemoAdapter(this, lItems);
        lvItem.setAdapter(adapter);
    }

    private void initData() {
        ListItem bean;

        bean = new ListItem();
        bean.setTitle("RcyclerView 适配器");
        bean.setValue(RecyclerAdapterActivity.class);
        bean.setBundle(getRecyclerAdapter());
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("RecyclerView");
        bean.setValue(RecyclerActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("自定义进度条(水平、圆形)");
        bean.setValue(ProgressBarActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("六子飞");
        bean.setValue(SixGameActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("Activity与Service相互通信");
        bean.setValue(MessengerActivit.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("圆形头像实现");
        bean.setValue(CircleImageActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("二维码扫描");
        bean.setValue(ZxingActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("设置GridView的Item正方形");
        bean.setValue(SquareActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("Popup弹出方案合集");
        bean.setValue(PopupActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("高清加载巨图方案");
        bean.setValue(LargeImageViewActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("五子棋游戏");
        bean.setValue(GobangActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("ViewPager指示器");
        bean.setValue(ViewPagerIndicatorActivity.class);
        lItems.add(bean);

        if (AppUtils.isInstallApp(this, APP_MUSIC)) {//AppUtils.hasApp(this, APP_MUSIC)
            bean = new ListItem();
            bean.setTitle("猜歌游戏");
            bean.setType(ListItem.TYPE_APP);
            bean.setValue(APP_MUSIC);
            lItems.add(bean);
        }

        bean = new ListItem();
        bean.setTitle("注解、反射使用案例");
        bean.setValue(AnnotationActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("心愿分享");
        bean.setValue(WishActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("拼图游戏");
        bean.setValue(JigsawActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("图灵机器人测试");
        bean.setValue(TulingActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("自定义相机测试");
        bean.setValue(CameraMainActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("AndroidAnnotaions框架测试");
        bean.setValue(AnnotationsActivity_.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("转盘抽奖");
        bean.setValue(LuckyDialActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("DialogUtils工具类测试");
        bean.setValue(DialogActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("带索引条的ListView");
        bean.setValue(ListViewIndexActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("刮刮卡");
        bean.setValue(ScratchCardActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("个性图片预览与多点触控");
        bean.setValue(ImagePreviewActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("仿微信聊天");
        bean.setValue(WeChatTalkActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("仿微信图片上传");
        bean.setValue(ImageLoaderActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("九宫格方案集锦");
        bean.setValue(SecondaryActivity.class);
        bean.setBundle(getNineLockData());
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("下拉刷新组件");
        bean.setValue(ListViewRfreshActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("流式布局组件");
        bean.setValue(FlowLayoutActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("轮播图片、广告");
        bean.setValue(ImageCycleViewActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("断点续传service");
        bean.setValue(DownServiceActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("万能适配器的实现");
        bean.setValue(AdapterAcitivty.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("任一级树形控件");
        bean.setValue(TreeActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("图片处理(美图秀秀)");
        bean.setValue(ImageMain.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("自定义title测试");
        bean.setValue(TitleBarActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("ViewPager 切换动画  自定义viewpage实现向下兼容");
        bean.setValue(ViewPagerCustormerActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("ViewPager切换动画");
        bean.setValue(ViewPagerActivity.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("主界面实现方案集锦");
        bean.setValue(SecondaryActivity.class);
        bean.setBundle(getMainViewData());
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("星型菜单(使用普通动画实现)");
        bean.setValue(StartMenu2.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("星型菜单(使用属性动画实现)");
        bean.setValue(StartMenu.class);
        lItems.add(bean);

        bean = new ListItem();
        bean.setTitle("倒计时");
        bean.setValue(TimerCountActivity.class);
        lItems.add(bean);

        adapter.notifyDataSetChanged();
    }

    /**
     * 九宫格实现集锦
     *
     * @return
     */
    private Bundle getNineLockData() {
        ArrayList<ListItem> lData = new ArrayList<ListItem>();
        ListItem bean;

        bean = new ListItem();
        bean.setTitle("九宫格解锁3");
        bean.setValue(Lock3Activity.class);
        lData.add(bean);

        bean = new ListItem();
        bean.setTitle("九宫格解锁2");
        bean.setValue(LockTestActivity.class);
        lData.add(bean);

        bean = new ListItem();
        bean.setTitle("九宫格解锁");
        bean.setValue(LockPatternActivity.class);
        lData.add(bean);

        bean = new ListItem();
        bean.setTitle("当前APP九宫格解锁设置");
        bean.setValue(LockActivity.class);
        lData.add(bean);

        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA_BUNDLE, lData);
        return bundle;
    }

    /**
     * 主机面集锦
     *
     * @return
     */
    private Bundle getMainViewData() {
        ArrayList<ListItem> lData = new ArrayList<ListItem>();
        ListItem bean;

        bean = new ListItem();
        bean.setTitle("QQ5.0侧滑菜单");
        bean.setValue(SlidingActivity.class);
        lData.add(bean);

        bean = new ListItem();
        bean.setTitle("Fragment实现Tab功能");
        bean.setValue(FragmentTabActivity.class);
        lData.add(bean);

        bean = new ListItem();
        bean.setTitle("仿微信界面");
        bean.setValue(WeChatActivivty.class);
        lData.add(bean);

        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA_BUNDLE, lData);
        return bundle;
    }

    /**
     * RecyclerView万能适配器
     *
     * @return
     */
    private Bundle getRecyclerAdapter() {
        ArrayList<ListItem> lData = new ArrayList<ListItem>();
        ListItem bean;

        bean = new ListItem();
        bean.setTitle("MultiItemListViewActivity");
        bean.setValue(MultiItemListViewActivity.class);
        lData.add(bean);

        bean = new ListItem();
        bean.setTitle("RecyclerViewActivity");
        bean.setValue(RecyclerViewActivity.class);
        lData.add(bean);

        bean = new ListItem();
        bean.setTitle("MultiItemRvActivity");
        bean.setValue(MultiItemRvActivity.class);
        lData.add(bean);

        bean = new ListItem();
        bean.setTitle("RvWidthHeaderActivity");
        bean.setValue(RvWidthHeaderActivity.class);
        lData.add(bean);

        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA_BUNDLE, lData);
        return bundle;
    }

    private void initListener() {
        lvItem.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ListItem bean = (ListItem) parent.getItemAtPosition(position);

                switch (bean.getType()) {
                    case ListItem.TYPE_ACTIVITY:
                        if (null != bean.getValue()) {
                            Intent intent = new Intent();
                            intent.setClass(MainDemoActivity.this,
                                    (Class<?>) bean.getValue());

                            Bundle bundle = bean.getBundle();
                            if (null != bundle) {
                                intent.putExtras(bundle);
                            }

                            startActivity(intent);
                        } else {
                            Toast.makeText(MainDemoActivity.this, "即将开通……",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case ListItem.TYPE_APP:
                        AppUtils.startApp(MainDemoActivity.this,
                                (String) bean.getValue());
                        break;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showToast(this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
