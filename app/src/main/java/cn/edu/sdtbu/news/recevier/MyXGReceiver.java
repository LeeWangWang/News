package cn.edu.sdtbu.news.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.android.tpush.NotificationAction;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyXGReceiver extends XGPushBaseReceiver {

	private static final String LogTag = "MyXGReceiver";

	@Override
	public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

	}

	@Override
	public void onUnregisterResult(Context context, int i) {

	}

	@Override
	public void onSetTagResult(Context context, int i, String s) {

	}

	@Override
	public void onDeleteTagResult(Context context, int i, String s) {

	}

	@Override
	public void onSetAccountResult(Context context, int i, String s) {

	}

	@Override
	public void onDeleteAccountResult(Context context, int i, String s) {

	}

	@Override
	public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {

	}

	/**
	 * @param context
	 * @param message
	 */
	@Override
	public void onNotificationClickedResult(Context context, XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (message.getActionType() == NotificationAction.clicked.getType()) {
			// 通知在通知栏被点击 APP自己处理点击的相关动作
			text = "通知被打开 :" + message;
		} else if (message.getActionType() == NotificationAction.delete.getType()) {
			// 通知被清除 APP自己处理通知被清除后的相关动作
			text = "通知被清除 :" + message;
		}
		//Toast.makeText(context, "广播接收到通知被点击:" + message.toString(), Toast.LENGTH_SHORT).show();
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					Log.d(LogTag, "get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP自主处理的过程。
		Log.d(LogTag, text);
		//show(context, text);
	}

	@Override
	public void onNotificationShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

	}
}
