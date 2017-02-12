package com.example.test.base;

/**
 * 用于给出取消AyncHttpClient执行的任务
 * @author HKW2962
 *
 */
public abstract class AsyncHttpClientCancelTaskListener {
	// 停止任务
	public abstract void cancelTask();
}