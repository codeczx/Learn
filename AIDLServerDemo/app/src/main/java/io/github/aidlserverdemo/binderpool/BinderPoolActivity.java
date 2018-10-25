package io.github.aidlserverdemo.binderpool;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.github.aidlserverdemo.ISecurityCenter;
import io.github.aidlserverdemo.R;

/**
 * 同个app里的跨进程调用
 */
public class BinderPoolActivity extends AppCompatActivity {

	private static final String TAG = "BinderPoolActivity";
	private int pid = android.os.Process.myPid();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_binder_pool);

		new Thread(new Runnable() {
			@Override
			public void run() {
				doWork();
			}
		}).start();
	}

	private void doWork(){
		Log.i(TAG, "doWork pid:"+pid);
		BinderPool binderPool = BinderPool.getInstance(BinderPoolActivity.this);
		IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY);
		ISecurityCenter securityCenter = SecurityCenterImpl.asInterface(securityBinder);
		try {
			Log.i(TAG, "doWork: encrypt:"+securityCenter.encrypt("string123"));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
