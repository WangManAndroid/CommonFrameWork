package com.youliao.commonframework.log;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Logger is a wrapper of {@link android.util.Log}
 * But more pretty, simple and powerful
 */
public final class Logger {
  public static final int DEBUG = 3;
  public static final int ERROR = 6;
  public static final int ASSERT = 7;
  public static final int INFO = 4;
  public static final int VERBOSE = 2;
  public static final int WARN = 5;
  private static  boolean isDebug;
  private static final String DEFAULT_TAG = "PRETTYLOGGER";

  private static Printer printer = new LoggerPrinter();

  //no instance
  private Logger() {
  }

  /**
   * It is used to get the settings object in order to change settings
   *
   * @return the settings object
   */
  public static Settings init(Context context) {
    isDebug=isApkDebugable(context);
    return init(DEFAULT_TAG);
  }
  public static void setDebug(boolean debug){
    isDebug=debug;
  }
  public static boolean isApkDebugable(Context context) {
    try {
      ApplicationInfo info= context.getApplicationInfo();
      return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
    } catch (Exception e) {

    }
    return false;
  }
  /**
   * It is used to change the tag
   *
   * @param tag is the given string which will be used in Logger as TAG
   */
  public static Settings init(String tag) {
    printer = new LoggerPrinter();
    return printer.init(tag);
  }

  public static void resetSettings() {
    printer.resetSettings();
  }

  public static Printer t(String tag) {
    return printer.t(tag, printer.getSettings().getMethodCount());
  }

  public static Printer t(int methodCount) {
    return printer.t(null, methodCount);
  }

  public static Printer t(String tag, int methodCount) {
    return printer.t(tag, methodCount);
  }

  public static void log(int priority, String tag, String message, Throwable throwable) {
    if (isDebug)
    printer.log(priority, tag, message, throwable);
  }

  public static void d(String message, Object... args) {
    if (isDebug)printer.d(message, args);
  }

  public static void d(Object object) {
    if (isDebug)printer.d(object);
  }

  public static void e(String message, Object... args) {
    if (isDebug)printer.e(null, message, args);
  }

  public static void e(Throwable throwable, String message, Object... args) {
    if (isDebug) printer.e(throwable, message, args);
  }

  public static void i(String message, Object... args) {
    if (isDebug)printer.i(message, args);
  }

  public static void v(String message, Object... args) {
    if (isDebug)printer.v(message, args);
  }

  public static void w(String message, Object... args) {
    if (isDebug)printer.w(message, args);
  }

  public static void wtf(String message, Object... args) {
    if (isDebug)printer.wtf(message, args);
  }

  /**
   * Formats the json content and print it
   *
   * @param json the json content
   */
  public static void json(String json) {
    if (isDebug)printer.json(json);
  }

  /**
   * Formats the json content and print it
   *
   * @param xml the xml content
   */
  public static void xml(String xml) {
    if (isDebug)printer.xml(xml);
  }

}
