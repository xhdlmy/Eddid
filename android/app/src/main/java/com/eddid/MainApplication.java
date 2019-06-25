package com.eddid;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;

import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage()
      );
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  public static Context sContext;
  public static int sVersionCode;
  public static String sVersionName;
  public static String sPackageName;
  public static String sChannelId;

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    sContext = this;
    initBuildConfig();
    initMta();
  }

  private void initMta() {
    StatConfig.setDebugEnable(isApkInDebug(sContext));
    // 数据发送策略
    StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);
    StatConfig.setSendPeriodMinutes(5);
    // TLink 推广功能
    StatConfig.setTLinkStatus(true);
    // 最好手动设置渠道
    StatConfig.setInstallChannel(sContext, sChannelId);
    StatService.registerActivityLifecycleCallbacks(this);
  }

  public static boolean isApkInDebug(Context context) {
    try {
      ApplicationInfo info = context.getApplicationInfo();
      return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    } catch (Exception e) {
      return false;
    }
  }

  private void initBuildConfig() {
    sVersionCode = BuildConfig.VERSION_CODE;
    sVersionName = BuildConfig.VERSION_NAME;
    sPackageName = BuildConfig.APPLICATION_ID;
    sChannelId = BuildConfig.FLAVOR;
  }

}
