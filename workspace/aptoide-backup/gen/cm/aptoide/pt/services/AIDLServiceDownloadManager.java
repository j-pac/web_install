/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/brutus/workspace/aptoide-backup/src/cm/aptoide/pt/services/AIDLServiceDownloadManager.aidl
 */
package cm.aptoide.pt.services;
/**
 * AIDLServiceDownloadManager, IPC Interface definition for Aptoide's ServiceDownloadManager
 *
 * @author dsilveira
 *
 */
public interface AIDLServiceDownloadManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements cm.aptoide.pt.services.AIDLServiceDownloadManager
{
private static final java.lang.String DESCRIPTOR = "cm.aptoide.pt.services.AIDLServiceDownloadManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cm.aptoide.pt.services.AIDLServiceDownloadManager interface,
 * generating a proxy if needed.
 */
public static cm.aptoide.pt.services.AIDLServiceDownloadManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof cm.aptoide.pt.services.AIDLServiceDownloadManager))) {
return ((cm.aptoide.pt.services.AIDLServiceDownloadManager)iin);
}
return new cm.aptoide.pt.services.AIDLServiceDownloadManager.Stub.Proxy(obj);
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
case TRANSACTION_callRegisterDownloadManager:
{
data.enforceInterface(DESCRIPTOR);
cm.aptoide.pt.AIDLDownloadManager _arg0;
_arg0 = cm.aptoide.pt.AIDLDownloadManager.Stub.asInterface(data.readStrongBinder());
this.callRegisterDownloadManager(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_callUnregisterDownloadManager:
{
data.enforceInterface(DESCRIPTOR);
this.callUnregisterDownloadManager();
reply.writeNoException();
return true;
}
case TRANSACTION_callRegisterDownloadObserver:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
cm.aptoide.pt.AIDLDownloadObserver _arg1;
_arg1 = cm.aptoide.pt.AIDLDownloadObserver.Stub.asInterface(data.readStrongBinder());
this.callRegisterDownloadObserver(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_callUnregisterDownloadObserver:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.callUnregisterDownloadObserver(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_callInstallApp:
{
data.enforceInterface(DESCRIPTOR);
cm.aptoide.pt.views.ViewDownloadManagement _arg0;
if ((0!=data.readInt())) {
_arg0 = cm.aptoide.pt.views.ViewDownloadManagement.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.callInstallApp(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_callGetAppDownloading:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
cm.aptoide.pt.views.ViewDownloadManagement _result = this.callGetAppDownloading(_arg0);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_callStartDownload:
{
data.enforceInterface(DESCRIPTOR);
cm.aptoide.pt.views.ViewDownloadManagement _arg0;
if ((0!=data.readInt())) {
_arg0 = cm.aptoide.pt.views.ViewDownloadManagement.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.callStartDownload(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_callStartDownloadAndObserve:
{
data.enforceInterface(DESCRIPTOR);
cm.aptoide.pt.views.ViewDownloadManagement _arg0;
if ((0!=data.readInt())) {
_arg0 = cm.aptoide.pt.views.ViewDownloadManagement.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
cm.aptoide.pt.AIDLDownloadObserver _arg1;
_arg1 = cm.aptoide.pt.AIDLDownloadObserver.Stub.asInterface(data.readStrongBinder());
this.callStartDownloadAndObserve(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_callPauseDownload:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.callPauseDownload(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_callResumeDownload:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.callResumeDownload(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_callStopDownload:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.callStopDownload(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_callRestartDownload:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.callRestartDownload(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_callAreDownloadsOngoing:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.callAreDownloadsOngoing();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_callGetDownloadsOngoing:
{
data.enforceInterface(DESCRIPTOR);
cm.aptoide.pt.views.ViewListDownloads _result = this.callGetDownloadsOngoing();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_callAreDownloadsCompleted:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.callAreDownloadsCompleted();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_callGetDownloadsCompleted:
{
data.enforceInterface(DESCRIPTOR);
cm.aptoide.pt.views.ViewListDownloads _result = this.callGetDownloadsCompleted();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_callAreDownloadsFailed:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.callAreDownloadsFailed();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_callGetDownloadsFailed:
{
data.enforceInterface(DESCRIPTOR);
cm.aptoide.pt.views.ViewListDownloads _result = this.callGetDownloadsFailed();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_callClearDownloads:
{
data.enforceInterface(DESCRIPTOR);
this.callClearDownloads();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements cm.aptoide.pt.services.AIDLServiceDownloadManager
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
@Override public void callRegisterDownloadManager(cm.aptoide.pt.AIDLDownloadManager downloadManager) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((downloadManager!=null))?(downloadManager.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_callRegisterDownloadManager, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void callUnregisterDownloadManager() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_callUnregisterDownloadManager, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void callRegisterDownloadObserver(int appHashId, cm.aptoide.pt.AIDLDownloadObserver downloadObserver) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(appHashId);
_data.writeStrongBinder((((downloadObserver!=null))?(downloadObserver.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_callRegisterDownloadObserver, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void callUnregisterDownloadObserver(int appHashId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(appHashId);
mRemote.transact(Stub.TRANSACTION_callUnregisterDownloadObserver, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void callInstallApp(cm.aptoide.pt.views.ViewDownloadManagement apk) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((apk!=null)) {
_data.writeInt(1);
apk.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_callInstallApp, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public cm.aptoide.pt.views.ViewDownloadManagement callGetAppDownloading(int appHashId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
cm.aptoide.pt.views.ViewDownloadManagement _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(appHashId);
mRemote.transact(Stub.TRANSACTION_callGetAppDownloading, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = cm.aptoide.pt.views.ViewDownloadManagement.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void callStartDownload(cm.aptoide.pt.views.ViewDownloadManagement download) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((download!=null)) {
_data.writeInt(1);
download.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_callStartDownload, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void callStartDownloadAndObserve(cm.aptoide.pt.views.ViewDownloadManagement download, cm.aptoide.pt.AIDLDownloadObserver downloadObserver) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((download!=null)) {
_data.writeInt(1);
download.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStrongBinder((((downloadObserver!=null))?(downloadObserver.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_callStartDownloadAndObserve, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void callPauseDownload(int appHashId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(appHashId);
mRemote.transact(Stub.TRANSACTION_callPauseDownload, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void callResumeDownload(int appHashId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(appHashId);
mRemote.transact(Stub.TRANSACTION_callResumeDownload, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void callStopDownload(int appHashId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(appHashId);
mRemote.transact(Stub.TRANSACTION_callStopDownload, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void callRestartDownload(int appHashId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(appHashId);
mRemote.transact(Stub.TRANSACTION_callRestartDownload, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean callAreDownloadsOngoing() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_callAreDownloadsOngoing, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public cm.aptoide.pt.views.ViewListDownloads callGetDownloadsOngoing() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
cm.aptoide.pt.views.ViewListDownloads _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_callGetDownloadsOngoing, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = cm.aptoide.pt.views.ViewListDownloads.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean callAreDownloadsCompleted() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_callAreDownloadsCompleted, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public cm.aptoide.pt.views.ViewListDownloads callGetDownloadsCompleted() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
cm.aptoide.pt.views.ViewListDownloads _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_callGetDownloadsCompleted, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = cm.aptoide.pt.views.ViewListDownloads.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean callAreDownloadsFailed() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_callAreDownloadsFailed, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public cm.aptoide.pt.views.ViewListDownloads callGetDownloadsFailed() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
cm.aptoide.pt.views.ViewListDownloads _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_callGetDownloadsFailed, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = cm.aptoide.pt.views.ViewListDownloads.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void callClearDownloads() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_callClearDownloads, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_callRegisterDownloadManager = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_callUnregisterDownloadManager = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_callRegisterDownloadObserver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_callUnregisterDownloadObserver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_callInstallApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_callGetAppDownloading = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_callStartDownload = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_callStartDownloadAndObserve = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_callPauseDownload = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_callResumeDownload = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_callStopDownload = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_callRestartDownload = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_callAreDownloadsOngoing = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_callGetDownloadsOngoing = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_callAreDownloadsCompleted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_callGetDownloadsCompleted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_callAreDownloadsFailed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_callGetDownloadsFailed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_callClearDownloads = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
}
public void callRegisterDownloadManager(cm.aptoide.pt.AIDLDownloadManager downloadManager) throws android.os.RemoteException;
public void callUnregisterDownloadManager() throws android.os.RemoteException;
public void callRegisterDownloadObserver(int appHashId, cm.aptoide.pt.AIDLDownloadObserver downloadObserver) throws android.os.RemoteException;
public void callUnregisterDownloadObserver(int appHashId) throws android.os.RemoteException;
public void callInstallApp(cm.aptoide.pt.views.ViewDownloadManagement apk) throws android.os.RemoteException;
public cm.aptoide.pt.views.ViewDownloadManagement callGetAppDownloading(int appHashId) throws android.os.RemoteException;
public void callStartDownload(cm.aptoide.pt.views.ViewDownloadManagement download) throws android.os.RemoteException;
public void callStartDownloadAndObserve(cm.aptoide.pt.views.ViewDownloadManagement download, cm.aptoide.pt.AIDLDownloadObserver downloadObserver) throws android.os.RemoteException;
public void callPauseDownload(int appHashId) throws android.os.RemoteException;
public void callResumeDownload(int appHashId) throws android.os.RemoteException;
public void callStopDownload(int appHashId) throws android.os.RemoteException;
public void callRestartDownload(int appHashId) throws android.os.RemoteException;
public boolean callAreDownloadsOngoing() throws android.os.RemoteException;
public cm.aptoide.pt.views.ViewListDownloads callGetDownloadsOngoing() throws android.os.RemoteException;
public boolean callAreDownloadsCompleted() throws android.os.RemoteException;
public cm.aptoide.pt.views.ViewListDownloads callGetDownloadsCompleted() throws android.os.RemoteException;
public boolean callAreDownloadsFailed() throws android.os.RemoteException;
public cm.aptoide.pt.views.ViewListDownloads callGetDownloadsFailed() throws android.os.RemoteException;
public void callClearDownloads() throws android.os.RemoteException;
}
