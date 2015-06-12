package com.src.playtime.thumb.blueService;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressLint("NewApi")
public class BlueService extends Service {
	public final IBinder mBinder = new MyBinder();
	private BluetoothManager mBluetoothManager;

	public static final int STATE_STARTCONNECT = 0; // 设备初始化
	public static final int STATE_DISCONNECTED = 1; // 设备未连接
	public static final int STATE_CONNECTING = 2; // 设备正在连接
	public static final int STATE_CONNECTED = 3; // 设备已连接
	public static final int STATE_GET_UI = 4; // UI就位
	public static final int STATE_SCANDEVICE_FAIL = 5;// 没有扫描到设备

	private int mConnectionState = STATE_DISCONNECTED;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// mApp = (MyApplication) getApplication();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	public class MyBinder extends Binder {

		public BlueService getService() {
			return BlueService.this;
		}
	}

	private BluetoothAdapter mBluetoothAdapter;
	private String mBluetoothDeviceAddress;
	private BluetoothGatt mBluetoothGatt;
	public boolean isBlueDeviceConnected = false;

	private BluetoothGattDescriptor descriptor;

	// private ArrayAdapter<String> mArrayAdapter = null;

	private ArrayList<BluetoothDevice> btDevices = null;

	public boolean isConnected() {
		if (!isBlueDeviceConnected) {
			return false;
		}
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			return false;
		}
		if (blueDeviceUI == 0) {
			return false;
		}
		return true;
	}

	public boolean initialize() {
		// For API level 18 and above, get a reference to BluetoothAdapter
		// through
		// BluetoothManager.
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			return false;
		}

		return true;
	}

	public boolean connect(String address) {
		// 如果蓝牙适配器为空、地址为空，则返回false
		if (mBluetoothAdapter == null || address == null) {
			isBlueDeviceConnected = false;
			sendBroadcast(new Intent(CONNECTED_STATE_BROADCAST).putExtra(
					"connected_state", STATE_DISCONNECTED));
			return false;
		}
		// 如果设备正在连接，且地址已经初始化，则返回true，否则false
		if (mBluetoothDeviceAddress != null
				&& address.equals(mBluetoothDeviceAddress)
				&& mBluetoothGatt != null) {
			if (mBluetoothGatt.connect()) {
				mConnectionState = STATE_CONNECTING;
				return true;
			} else {
				isBlueDeviceConnected = false;
				sendBroadcast(new Intent(CONNECTED_STATE_BROADCAST).putExtra(
						"connected_state", STATE_DISCONNECTED));
				return false;
			}
		}
		// 用适配器连接地址，获得蓝牙设备，若设备为空，则返回false，否则记录地址，设置状态为已连接，返回true
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("21:11:76:88:99:99");
		if (device == null) {
			isBlueDeviceConnected = false;
			sendBroadcast(new Intent(CONNECTED_STATE_BROADCAST).putExtra(
					"connected_state", STATE_DISCONNECTED));
			return false;
		}
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		if (mBluetoothGatt.connect()) {
			Log.e("-----", "蓝牙已连接");
			// sendBroadcast(new Intent(CONNECTED_STATE_BROADCAST).putExtra(
			// "connected_state", STATE_CONNECTED));
		}

		mBluetoothDeviceAddress = address;
		mConnectionState = STATE_CONNECTED;
		return true;

	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	// 各种操作的回调
	BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// 读操作
			Log.e("fanhui", "vvvvvvvvvvvvvvvvvvv"
					+ parseData(characteristic));

		};

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.e("fanhui","onCharacteristicWrite--"+parseData(characteristic));
            readCharacteristic(write_charact);

        }

        public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			// 根据状态执行操作
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				Log.i("tag", "已连接");

				isBlueDeviceConnected = true;

				sendBroadcast(new Intent(CONNECTED_STATE_BROADCAST).putExtra(
						"connected_state", STATE_CONNECTED));
				// 连接之后扫描设备服务
				mBluetoothGatt.discoverServices();
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				Log.i("tag", "已断开");
				isBlueDeviceConnected = false;
				sendBroadcast(new Intent(CONNECTED_STATE_BROADCAST).putExtra(
						"connected_state", STATE_DISCONNECTED));
			}
		}

		// 扫描监听
		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			// TODO Auto-generated method stub
			super.onServicesDiscovered(gatt, status);
			// 扫描到服务之后发送广播通知界面
			// sendBroadcast(new Intent(BlueUI1.GET_RECEIVER).putExtra(
			// "getData", BlueUI1.GET_SERVICE));
			// 初始化指令集
			initCharact();
		};

		// 对监听的设备信息做处理
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			// // 获得到通知数据之后发送广播通知界面
			// sendBroadcast(new
			// Intent(ActivityBlueContro.GET_RECEIVER).putExtra(
			// "getData", ActivityBlueContro.GET_NOTIFY));
			// int data = parseData(characteristic);
			// dealData(data);
            Log.e("fanhui","onCharacteristicChanged---"+parseData(characteristic));
		}

	};
	public BluetoothAdapter blueAdapter;

	// 读操作
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic);
	}

	// 写操作
	public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.writeCharacteristic(characteristic);

	}

	// 监听
	public void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
		// descriptor = characteristic.getDescriptor(UUID
		// .fromString(SampleGattAttributes.DESCRIPTION_NOTIFY));
		// descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		// mBluetoothGatt.writeDescriptor(descriptor);

	}

	// 得到设备扫描到的所有服务
	@SuppressLint("NewApi")
	public List<BluetoothGattService> getSupportedGattServices() {
		if (mBluetoothGatt == null)
			return null;

		return mBluetoothGatt.getServices();
	}

	// 得到指定的设备服务
	public BluetoothGattService getSupportedGattService(String uuid) {
		if (mBluetoothGatt == null)
			return null;
		return mBluetoothGatt.getService(UUID.fromString(uuid));
	}

	// 断开设备连接
	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.disconnect();
	}

	// 关闭管理器
	public void close() {
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}

	/*********** 扫描蓝牙设备 ******************/

	public void initDevice() {

		final boolean isKitKat = Build.VERSION.SDK_INT >= 18;
		if (!isKitKat) {
			Toast.makeText(this, "本蓝牙设备只支持Android4.3以上版本", Toast.LENGTH_LONG)
					.show();
			return;
		}
		init();
		scanBlue();

	}

	@SuppressLint("NewApi")
	protected boolean init() {

		// 判断是否支持BLE
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, "你的设备不支持BLE", Toast.LENGTH_SHORT).show();
			return false;
		}

		// 获得蓝牙管理器和蓝牙适配器
		mBluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
		blueAdapter = mBluetoothManager.getAdapter();

		// 开启蓝牙模块
		if (blueAdapter == null || !blueAdapter.isEnabled()) {
            blueAdapter.enable();
//			Intent enableBlueIntent = new Intent(
//					BluetoothAdapter.ACTION_REQUEST_ENABLE);
//			startActivity(enableBlueIntent
//					.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			return false;
		}
		return true;

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHECK_UI:// 检查UI指令知否就位
				if (blueDeviceUI == 0) {// 没有收到UI指令
					sendOrder(SampleGattAttributes.getOrder(211));
				} else {// 收到UI指令，返回确认信息
					sendOrder(SampleGattAttributes.getOrder(210));
				}
				break;
			default:
				break;
			}
		};
	};
	// private boolean isScan;
	private final int ACTION_REQUEST_ENABLE = 0; // 开启蓝牙
	private final int ACTION_SCAN_TIME = 5000; // 扫描时间

	public final static String LOW_BATTERY_BROADCAST = "LOW_BATTERY_BROADCAST"; // 电池广播
	public final static String CONNECTED_STATE_BROADCAST = "CONNECTED_STATE_BROADCAST"; // 连接状态广播

	private final int CHECK_UI = 0;

	private String mDeviceName;
	private String mDeviceAddress = "";

	public int blueDeviceUI = 0;
	public int battery = 0;

	/**
	 * 是否开始扫描设备，扫描超时时间之后停止扫描
	 *
	 */
	@SuppressLint("NewApi")
	public void scanBlue() {
		disconnect();
		if (blueAdapter != null && blueAdapter.isEnabled()) {
			// 开始扫描蓝牙
			sendBroadcast(new Intent(CONNECTED_STATE_BROADCAST).putExtra(
					"connected_state", STATE_STARTCONNECT));

			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// isScan = false;
					blueAdapter.stopLeScan(mLeScanCallback);
					if (mDeviceAddress.equals("")) {
						sendBroadcast(new Intent(CONNECTED_STATE_BROADCAST)
								.putExtra("connected_state",
										STATE_SCANDEVICE_FAIL));
					}

				}
			}, ACTION_SCAN_TIME);
			/** 连接指定的设备用这个方法 */
			// isScan = true;
//			blueAdapter.startLeScan(new UUID[] { UUID
//			.fromString(SampleGattAttributes.GOD_1) },
//			mLeScanCallback);
			blueAdapter.startLeScan(mLeScanCallback);

			// else {
			// // isScan = false;
			// blueAdapter.stopLeScan(mLeScanCallback);
			// Toast.makeText(this, "未扫描到设备", Toast.LENGTH_LONG).show();
			// Log.e("TAG", "未扫描到设备");
			// sendBroadcast(new Intent(CONNECTED_STATE_BROADCAST).putExtra(
			// "connected_state", STATE_SCANDEVICE_FAIL));
			// }
		}
	}

	@SuppressLint("NewApi")
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi,
				byte[] scanRecord) {
			mDeviceName = device.getName();
			mDeviceAddress = device.getAddress();
            Log.e("UUIDs","---"+device.getUuids()+"address"+device.getAddress());
			// 连接设备
			if (!initialize()) {
				// Toast.makeText(this, "初始化失败", 0).show();
				return;
			}
			if (connect(mDeviceAddress)) {
			} else {
				 //Toast.makeText(this, "连接设备失败", 0).show();
				return;
			}
		}

	};
	private BluetoothGattService write_service;
	private BluetoothGattCharacteristic write_charact;
	private BluetoothGattService notify_service;
	private BluetoothGattCharacteristic notify_charact;

	private void initCharact() {
		List<BluetoothGattService> services = getSupportedGattServices();
		if (getSupportedGattServices().size() != 0) {
			// PromptManager.showToast(mApp, "开启指令通道");
			// write_service =
			// getSupportedGattService(SampleGattAttributes.SERVICE_WRITE);
            Log.e("---","初始化指令集");
			notify_service = getSupportedGattService(SampleGattAttributes.SERVICE_NOTIFY);
            //notify_service.getCharacteristics();
            write_charact = notify_service.getCharacteristic(UUID
					.fromString(SampleGattAttributes.CHARACT_WRITE));
            notify_charact = notify_service.getCharacteristic(UUID
					.fromString(SampleGattAttributes.CHARACT_NOTIFY));

			// sendHandOrder();
			setNotify();// 设置监听
			// 发送握手指令
		}
	}

	/**
	 * 握手指令
	 */
	private void sendHandOrder() {
		// byte[] b = new byte[1];
		// b[0] = (byte) 205;
		// PromptManager.showLog("tagblue", SampleGattAttributes.getOrder(205)
		// .toString());
		sendOrder(SampleGattAttributes.getOrder(205));
	}

	/**
     * 发送指令集
     *
     * @param b
     *            指令
     */
    public void sendOrder(byte[] b,RequstBtCharacteristic requst) {
        if (notify_charact != null) {
            int prop = notify_charact.getProperties();
            if ((prop | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                // write_charact.setWriteType(50);
                // int i=BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT;
                // write_charact.addDescriptor(descriptor);
                // write_charact.setValue(b);
                // try {
                if(requst!=null){

                }
                notify_charact.setValue(b);
                // } catch (UnsupportedEncodingException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                writeCharacteristic(notify_charact);
                //readCharacteristic(write_charact);
            }
        }
    }

    /**
     * 发送指令集
     *
     * @param b
     *            指令
     */
    public void sendOrder(byte[] b) {
        if (notify_charact != null) {
            int prop = notify_charact.getProperties();
            if ((prop | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                // write_charact.setWriteType(50);
                // int i=BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT;
                // write_charact.addDescriptor(descriptor);
                // write_charact.setValue(b);
                // try {
                notify_charact.setValue(b);
                // } catch (UnsupportedEncodingException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                writeCharacteristic(notify_charact);
                //readCharacteristic(write_charact);
            }
        }
    }



	public byte[] parseHexStringToBytes(String paramString) {
		String str = paramString.substring(2).replaceAll("[^[0-9][a-f]]", "");
		byte[] arrayOfByte = new byte[str.length() / 2];
		for (int i = 0;; i++) {
			if (i >= arrayOfByte.length)
				return arrayOfByte;
			arrayOfByte[i] = Long
					.decode("0x" + str.substring(i * 2, 2 + i * 2)).byteValue();
		}
	}

	// 设置监听
	private void setNotify() {
		if (write_charact != null) {
			int prop = write_charact.getProperties();
			if ((prop | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
				setCharacteristicNotification(write_charact, true);
			}
		}
	}

	/**
	 * 解析数据
	 * 
	 * @param characteristic
	 * @return 返回指令
	 */
	private String parseData(BluetoothGattCharacteristic characteristic) {
		byte[] value = characteristic.getValue();
        String s = "";
        for (int i = 0; i <value.length ; i++) {
            String binaryString = Integer.toBinaryString(value[i]);
            int length = binaryString.length();
            String x = length > 8 ? binaryString.substring(length - 8, length)
                    : binaryString;
            int temp = 0;
            try {
                temp = Integer.valueOf(x, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            s+=temp;
        }

		return s;
	};

	/**
	 * 处理指令
	 * 
	 * @param data
	 */
	private void dealData(int data) {
		Log.e("tagblue", "data===" + data);
		// 一秒之后检查有没有收到UI指令
		Log.e("tagblue", "blueDeviceUI=" + blueDeviceUI);
		if (blueDeviceUI == 0) {
			mHandler.sendEmptyMessageDelayed(CHECK_UI, 500);
		}
		// UI就位
		if (data > 0 && data < 51) {
			blueDeviceUI = data;
			// PromptManager.showLog("tag", "获取UI界面：" + blueDeviceUI);
			// sendOrder(SampleGattAttributes.getOrder(210));
			sendBroadcast(new Intent(CONNECTED_STATE_BROADCAST).putExtra(
					"connected_state", STATE_GET_UI));
			// mHandler.sendEmptyMessageDelayed(CHECK_UI, 500);

			// 接收电量
		}
		if (data > 100 && data < 201) {
			// 设置电量，向外发送广播
			battery = data;
			sendBroadcast(new Intent(LOW_BATTERY_BROADCAST).putExtra(
					"getBattery", (battery - 100)));
			// PromptManager.showLog("blue", "剩余电量：" + (battery - 100));
		}

	}

	public int getBattery() {
		if (battery != 0) {
			return battery - 100;
		}
		return 0;
	}

    public interface RequstBtCharacteristic{

        public void getCharacteristic(BluetoothGattCharacteristic characteristic);

    }

}
