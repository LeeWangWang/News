package cn.edu.sdtbu.news.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JIGUANG-Example";

	@Override
	public void onReceive(Context context, Intent intent) {


	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		//for (String key : bundle.keySet()) {
		//	if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
		//		sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
		//	}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
		//		sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
		//	} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
		//		if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
		//			Logger.i(TAG, "This message has no Extra data");
		//			continue;
		//		}
		//
		//		try {
		//			JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
		//			Iterator<String> it =  json.keys();
		//
		//			while (it.hasNext()) {
		//				String myKey = it.next();
		//				sb.append("\nkey:" + key + ", value: [" +
		//						myKey + " - " +json.optString(myKey) + "]");
		//			}
		//		} catch (JSONException e) {
		//			Logger.e(TAG, "Get message extra JSON error!");
		//		}
		//
		//	} else {
		//		sb.append("\nkey:" + key + ", value:" + bundle.get(key));
		//	}
		//}
		return sb.toString();
	}
	
	//send msg to MainActivity
	//private void processCustomMessage(Context context, Bundle bundle) {
	//	if (MainActivity.isForeground) {
	//		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
	//		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
	//		Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
	//		msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
	//		if (!ExampleUtil.isEmpty(extras)) {
	//			try {
	//				JSONObject extraJson = new JSONObject(extras);
	//				if (extraJson.length() > 0) {
	//					msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
	//				}
	//			} catch (JSONException e) {
	//
	//			}
	//
	//		}
	//		LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
	//	}
	//}
}
