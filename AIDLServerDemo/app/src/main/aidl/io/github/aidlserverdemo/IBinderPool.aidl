// IBinderPool.aidl
package io.github.aidlserverdemo;

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
