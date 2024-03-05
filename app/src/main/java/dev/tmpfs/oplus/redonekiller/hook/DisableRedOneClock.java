package dev.tmpfs.oplus.redonekiller.hook;

import androidx.annotation.NonNull;

import static dev.tmpfs.oplus.redonekiller.util.ReflexUtils.loadClassOrNull;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.TextView;

import java.lang.reflect.Method;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import dev.tmpfs.oplus.redonekiller.util.Log;

public class DisableRedOneClock {

    private DisableRedOneClock() {
    }

    public static void init(@NonNull ClassLoader cl) throws ReflectiveOperationException {
        initHookForSystemUiClassLoader(cl);
        initHookForVariUiEnginePluginClassLoader(cl);
        initHookForAodPluginClassLoader(cl);
    }

    private static void initHookForSystemUiClassLoader(@NonNull ClassLoader cl) throws ReflectiveOperationException {
        Log.d("DisableRedOneClock.initHookForSystemUiDefaultClassLoader");
        // com.oplus.systemui.common.clock.OplusClockExImpl#setTextWithRedOneStyleInternal(TextView, CharSequence)V
        Class<?> kOplusClockExImpl = loadClassOrNull(cl, "com.oplus.systemui.common.clock.OplusClockExImpl");
        if (kOplusClockExImpl != null) {
            Method setTextWithRedOneStyleInternal = kOplusClockExImpl.getDeclaredMethod(
                    "setTextWithRedOneStyleInternal", TextView.class, CharSequence.class);
            XposedBridge.hookMethod(setTextWithRedOneStyleInternal, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    TextView textView = (TextView) param.args[0];
                    CharSequence text = (CharSequence) param.args[1];
                    if (textView != null && text != null) {
                        textView.setText(text.toString().replace(":", "\u200e\u2236"));
                        param.setResult(null);
                    }
                }
            });
        }
        // com.oplus.keyguard.utils.KeyguardUtils#getSpannedHourString
        Class<?> kKeyguardUtils = loadClassOrNull(cl, "com.oplus.keyguard.utils.KeyguardUtils");
        if (kKeyguardUtils != null) {
            Method getSpannedHourString = kKeyguardUtils.getDeclaredMethod("getSpannedHourString", Context.class, String.class);
            XposedBridge.hookMethod(getSpannedHourString, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    Context context = (Context) param.args[0];
                    String str = (String) param.args[1];
                    if (context != null && str != null) {
                        param.setResult(getSpannedHourString(context, str));
                    }
                }
            });
        }
    }

    private static void initHookForAodPluginClassLoader(@NonNull ClassLoader cl) throws ReflectiveOperationException {
        // surpirse! there is nothing to hook in AOD plugin
    }

    private static void initHookForVariUiEnginePluginClassLoader(@NonNull ClassLoader cl) throws ReflectiveOperationException {
        // com.oplus.egview.widget.OpKeyguardOplusTextView#getTextWithOplusColor(CharSequence)SpannableString
        Class<?> kOpKeyguardOplusTextView = loadClassOrNull(cl, "com.oplus.egview.widget.OpKeyguardOplusTextView");
        if (kOpKeyguardOplusTextView != null) {
            Method getTextWithOplusColor = kOpKeyguardOplusTextView.getDeclaredMethod(
                    "getTextWithOplusColor", CharSequence.class);
            XposedBridge.hookMethod(getTextWithOplusColor, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    CharSequence text = (CharSequence) param.args[0];
                    if (text != null) {
                        param.setResult(getTextWithOplusColor(text));
                    }
                }
            });
        }
    }

    public static SpannableStringBuilder getSpannedHourString(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return new SpannableStringBuilder("");
        }
        return new SpannableStringBuilder(str);
    }

    public static SpannableString getTextWithOplusColor(CharSequence text) {
        // the original method has a toString call, so we just keep it to avoid any potential issues
        return new SpannableString(text.toString());
    }

}
