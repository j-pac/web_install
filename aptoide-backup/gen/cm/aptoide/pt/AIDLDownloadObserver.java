/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/brutus/workspace/aptoide-backup/src/cm/aptoide/pt/AIDLDownloadObserver.aidl
 */
package cm.aptoide.pt;
/**
 * AIDLDownloadObserver, models Aptoide's download Observer AIDL IPC
 *
 * @author dsilveira
 *
 */
public interface AIDLDownloadObserver extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements cm.aptoide.pt.AIDLDownloadObserver
{
private static final java.lang.String DESCRIPTOR = "cm.aptoide.pt.AIDLDownloadObserver";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cm.aptoide.pt.AIDLDownloadObserver interface,
 * generating a proxy if needed.
 */
public static cm.aptoide.pt.AIDLDownloadObserver asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof cm.aptoide.pt.AIDLDownloadObserver))) {
return ((cm.aptoide.pt.AIDLDownloadObserver)iin);
}
return new cm.aptoide.pt.AIDLDownloadObserver.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_updateDownloadStatus:
{
data.enforceInterface(DESCRIPTOR);
cm.aptoide.pt.views.ViewDownload _arg0;
if ((0!=data.readInt())) {
_arg0 = cm.aptoide.pt.views.ViewDownload.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.updateDownloadStatus(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements cm.aptoide.pt.AIDLDownloadObserver
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void updateDownloadStatus(cm.aptoide.pt.views.ViewDownload update) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((update!=null)) {
_data.writeInt(1);
update.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_updateDownloadStatus, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_updateDownloadStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void updateDownloadStatus(cm.aptoide.pt.views.ViewDownload update) throws android.os.RemoteException;
}
