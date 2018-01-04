package cn.tongchengyuan.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.style.utils.CommonUtil;
import com.style.utils.FormatUtil;


public class UpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int RunCount = 0;
		if (RunCount % 10 == 0) {
			initUserList();
			initGroupList();

			String str_contact = null;
			PackageManager pm = getPackageManager();
			boolean permission = (PackageManager.PERMISSION_GRANTED == pm
					.checkPermission("android.permission.READ_CONTACTS",
							"com.juns.wechat"));
			if (TextUtils.isEmpty(str_contact) && permission) {
				str_contact = getContact();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	// 获取群组列表
	private void initGroupList() {


	}

	// 获取好友列表和订阅号
	private void initUserList() {



	}

	public String getContact() {
		// 获得所有的联系人
		String strTelphones = "";
		String strNames = "";
		Cursor cur = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		// 循环遍历
		if (cur.moveToFirst()) {
			int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
			int displayNameColumn = cur
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			do {
				// 获得联系人的ID号
				String contactId = cur.getString(idColumn);
				// 获得联系人姓名
				String disPlayName = cur.getString(displayNameColumn);
				// 查看该联系人有多少个电话号码。如果没有这返回值为0
				int phoneCount = cur
						.getInt(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				if (phoneCount > 0) {
					// 获得联系人的电话号码
					Cursor phones = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
					if (phones.moveToFirst()) {
						do { // 遍历所有的电话号码
							String phoneNumber = phones
									.getString(phones
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							if (phoneNumber.startsWith("+186")) {
								phoneNumber = phoneNumber.substring(4);
							}
							if (FormatUtil.isMobileNum(phoneNumber)) {
								strTelphones = strTelphones + "'" + phoneNumber
										+ "',";
								strNames = strNames + "',";
							}
						} while (phones.moveToNext());
					}
				}
			} while (cur.moveToNext());
		}
		if (strTelphones.length() > 0 && strNames.length() > 0) {
			strTelphones = strTelphones.substring(0, strTelphones.length() - 1);
			strNames = strNames.substring(0, strNames.length() - 1);
		}
		return strTelphones;
	}

	private void sendBrodcast(String Action) {
		Intent intent = new Intent();
		intent.setAction("com.juns.wechat.Brodcast");
		intent.putExtra("Action", Action);
		sendBroadcast(intent);
	}
}
