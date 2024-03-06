package dev.tmpfs.oplus.redonekiller.hook;

import static dev.tmpfs.oplus.redonekiller.util.ReflexUtils.loadClassOrNull;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import dev.tmpfs.oplus.redonekiller.util.Log;

public class HideAodSilentNotificationIcon {

    private static final StatusBarNotification[] EMPTY_NOTIFICATIONS = new StatusBarNotification[0];

    private HideAodSilentNotificationIcon() {
    }

    public static void init(@NonNull ClassLoader cl) throws ReflectiveOperationException {
        initHookForSystemUiClassLoader(cl);
        initHookForVariUiEnginePluginClassLoader(cl);
        initHookForAodPluginClassLoader(cl);
    }

    private static void initHookForSystemUiClassLoader(@NonNull ClassLoader cl) throws ReflectiveOperationException {
        // com.oplus.systemui.aod.common.AodNotificationListenerService
        Class<?> kAodNotificationListenerService = loadClassOrNull(cl, "com.oplus.systemui.aod.common.AodNotificationListenerService");
        if (kAodNotificationListenerService != null) {
            // AodNotificationListenerService#getActiveNotificationsWithFilter(NotificationListenerService.RankingMap)StatusBarNotification[]
            Method getActiveNotificationsWithFilter = kAodNotificationListenerService.getDeclaredMethod("getActiveNotificationsWithFilter", NotificationListenerService.RankingMap.class);
            XposedBridge.hookMethod(getActiveNotificationsWithFilter, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    NotificationListenerService context = (NotificationListenerService) param.thisObject;
                    NotificationListenerService.RankingMap ranking = (NotificationListenerService.RankingMap) param.args[0];
                    StatusBarNotification[] input = (StatusBarNotification[]) param.getResult();
                    param.setResult(filterActiveNotifications2(context, ranking, input));
                }
            });
            // AodNotificationListenerService#onNotificationPosted(StatusBarNotification, NotificationListenerService.RankingMap)V
            Method onNotificationPosted = kAodNotificationListenerService.getDeclaredMethod("onNotificationPosted",
                    StatusBarNotification.class, NotificationListenerService.RankingMap.class);
            XposedBridge.hookMethod(onNotificationPosted, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    NotificationListenerService context = (NotificationListenerService) param.thisObject;
                    StatusBarNotification sbn = (StatusBarNotification) param.args[0];
                    NotificationListenerService.RankingMap ranking = (NotificationListenerService.RankingMap) param.args[1];
                    if (sbn == null || ranking == null) {
                        return;
                    }
                    if (!shouldShowOnAod(context, sbn, getNotificationChannel(sbn.getKey(), ranking))) {
                        param.setResult(null);
                    }
                }
            });
        }
    }

    private static void initHookForVariUiEnginePluginClassLoader(@NonNull ClassLoader cl) throws ReflectiveOperationException {
        // no-op
    }

    private static void initHookForAodPluginClassLoader(@NonNull ClassLoader cl) throws ReflectiveOperationException {
        // no-op
    }

    private static NotificationChannel getNotificationChannel(String key, NotificationListenerService.RankingMap rankingMap) {
        NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
        rankingMap.getRanking(key, ranking);
        return ranking.getChannel();
    }

    private static StatusBarNotification[] filterActiveNotifications2(@NonNull NotificationListenerService context,
                                                                      NotificationListenerService.RankingMap ranking,
                                                                      StatusBarNotification[] input) {
        if (ranking == null || input == null || input.length == 0) {
            return input;
        }
        ArrayList<StatusBarNotification> output = new ArrayList<>(input.length);
        for (StatusBarNotification sbn : input) {
            if (shouldShowOnAod(context, sbn, getNotificationChannel(sbn.getKey(), ranking))) {
                output.add(sbn);
            }
        }
        return output.toArray(EMPTY_NOTIFICATIONS);
    }


    private static boolean shouldShowOnAod(@NonNull NotificationListenerService context,
                                           @NonNull StatusBarNotification notification,
                                           NotificationChannel channel) {
        if (channel != null) {
            // Log.d("channel: " + channel.getId() + ", importance: " + channel.getImportance() + "notification: " + notification);
            if (channel.getImportance() < NotificationManager.IMPORTANCE_HIGH) {
                return false;
            }
        } else {
            // Log.w("channel is null for " + notification);
        }
        return true;
    }

}
