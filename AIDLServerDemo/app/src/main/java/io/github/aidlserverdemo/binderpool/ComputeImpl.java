package io.github.aidlserverdemo.binderpool;

import android.os.RemoteException;

import io.github.aidlserverdemo.ICompute;

/**
 * Created by codeczx on 2018/10/24 下午 10:17.
 * Class description:
 */
public class ComputeImpl extends ICompute.Stub {
	@Override
	public int add(int a, int b) throws RemoteException {
		return a + b;
	}
}
