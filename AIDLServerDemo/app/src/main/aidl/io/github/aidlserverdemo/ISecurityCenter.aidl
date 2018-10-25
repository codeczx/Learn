// ISecurityCenter.aidl
package io.github.aidlserverdemo;

interface ISecurityCenter {
   String encrypt(String content);
   String decrypt(String password);
}
