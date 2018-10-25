package io.github.aidlclientdemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import io.github.aidlserverdemo.Book;
import io.github.aidlserverdemo.BookController;
import io.github.aidlserverdemo.IOnNewBookArrivedListener;

/**
 * 客户端，调用远程服务
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private static final String TAG = "MainActivity";
	private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

	private Button mBtnGetBookList;
	private Button mBtnAddBook;
	private BookController mBookController;
	private List<Book> mBookList;
	private boolean connected;

	private Handler mHandler = new MyHandler();

	private static class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_NEW_BOOK_ARRIVED:
					Log.i(TAG, "handleMessage: receive new book :" + msg.obj);
					break;
				default:
					super.handleMessage(msg);
			}
		}
	}

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBookController = BookController.Stub.asInterface(service);
			try {
				mBookController.registerListener(mIOnNewBookArrivedListener);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			connected = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			connected = false;
			mBookController = null;
		}
	};

	private IOnNewBookArrivedListener mIOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
		@Override
		public void onNewBookArrived(Book newBook) throws RemoteException {
			mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBtnGetBookList = findViewById(R.id.btn_get_book_list);
		mBtnAddBook = findViewById(R.id.btn_add_book);

		mBtnAddBook.setOnClickListener(this);
		mBtnGetBookList.setOnClickListener(this);

		bindService();
	}

	private void bindService() {
		Intent intent = new Intent();
		intent.setPackage("io.github.aidlserverdemo");
		intent.setAction("io.github.aidlserverdemo.action");
		bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_add_book:
				if (connected) {
					Book book = new Book("java编程思想");
					try {
						mBookController.addBookInOut(book);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				break;
			case R.id.btn_get_book_list:
				if (connected) {
					try {
						mBookList = mBookController.getBookList();
						Log.i(TAG, "onClick: " + mBookList.toString());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBookController != null && mBookController.asBinder().isBinderAlive()) {
			Log.i(TAG, "onDestroy: unregister listener:" + mIOnNewBookArrivedListener);
			try {
				mBookController.unregisterListener(mIOnNewBookArrivedListener);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		unbindService(mServiceConnection);
	}
}
