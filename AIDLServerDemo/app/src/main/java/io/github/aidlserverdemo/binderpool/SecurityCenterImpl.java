package io.github.aidlserverdemo.binderpool;

import android.os.RemoteException;
import android.util.Log;

import io.github.aidlserverdemo.ISecurityCenter;

/**
 * Created by codeczx on 2018/10/24 下午 10:16.
 * Class description:
 */
public class SecurityCenterImpl extends ISecurityCenter.Stub {

	private static final String TAG = "SecurityCenterImpl";
	private int pid = android.os.Process.myPid();

	@Override
	public String encrypt(String content) throws RemoteException {
		Log.i(TAG, "SecurityCenterImpl#encrypt pid:"+pid);
		return "---encrypt---" + content;
	}

	@Override
	public String decrypt(String password) throws RemoteException {
		Log.i(TAG, "SecurityCenterImpl#encrypt pid:"+pid);
		return "---decrypt---" + password;
	}
}
