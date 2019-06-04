package cn.jpush.api.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class PushExample {
	protected static final Logger LOG = LoggerFactory.getLogger(PushExample.class);

	// demo App defined in resources/jpush-api.conf
	private static final String appKey = "8835413d905d71bb1f5c7847";
	private static final String masterSecret = "ee2e1ddc65b69cbb8688ce6d";

	public static final String TITLE = "新订单";
	public static final String ALERT = "有新的订单要发货,点击抢单哦~";
	public static final String MSG_CONTENT = "";
	public static final String REGISTRATION_ID = "0900e8d85ef";
	public static final String TAG = "siji";

	public static void main(String[] args) {
		pushToUser("18503058916","我操");
		System.out.println("done");
	}

	public static void pushToUser(String telephone, String title) {
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
		PushPayload payload = buildPushObject_android_and_ios(telephone);;//(telephone,title);
		try {
			PushResult result = jpushClient.sendPush(payload);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testSendPush() {
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);

		// For push, all you need do is to build PushPayload object.
		PushPayload payload = buildPushObject_android_and_ios("");

		try {
			PushResult result = jpushClient.sendPush(payload);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static PushPayload buildPushObject_all_all_alert() {
		return PushPayload.alertAll(ALERT);
	}

	public static PushPayload buildPushObject_all_alias_alert(String telephone,String title) {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(Audience.tag(telephone))
				.setNotification(
						Notification
								.newBuilder()
								.setAlert(title)
								.addPlatformNotification(AndroidNotification.newBuilder().setTitle(title).addExtra("to_page", telephone).build())
								.addPlatformNotification(
										IosNotification.newBuilder().incrBadge(1).addExtra("to_page", telephone)
												.build()).build())
				.setOptions(Options.newBuilder().setApnsProduction(true).build()).build();
	}

	public static PushPayload buildPushObject_android_tag_alertWithTitle() {
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.tag("siji"))
				.setNotification(Notification.android(ALERT, TITLE, null)).build();
	}

	/**
	 * 使用Tag:siji来区别接受对象
	 * 
	 * @return
	 */
	public static PushPayload buildPushObject_android_and_ios(String telephone) {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(Audience.tag(telephone))
				.setNotification(
						Notification
								.newBuilder()
								.setAlert(ALERT)
								.addPlatformNotification(AndroidNotification.newBuilder().setTitle(ALERT).build())
								.addPlatformNotification(
										IosNotification.newBuilder().incrBadge(1).addExtra("to_page", "siji_order")
												.build()).build())
				.setOptions(Options.newBuilder().setApnsProduction(false).build()).build();
	}

	public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage() {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.ios())
				.setAudience(Audience.tag_and("tag1", "tag_all"))
				.setNotification(
						Notification
								.newBuilder()
								.addPlatformNotification(
										IosNotification.newBuilder().setAlert(ALERT).setBadge(5).setSound("happy")
												.addExtra("from", "JPush").build()).build())
				.setMessage(Message.content(MSG_CONTENT))
				.setOptions(Options.newBuilder().setApnsProduction(true).build()).build();
	}

	public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras() {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(
						Audience.newBuilder().addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))
								.addAudienceTarget(AudienceTarget.alias("alias1", "alias2")).build())
				.setMessage(Message.newBuilder().setMsgContent(MSG_CONTENT).addExtra("from", "JPush").build()).build();
	}

}
