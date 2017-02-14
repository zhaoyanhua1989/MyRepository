/* 文件头 */
#include "com_example_textjni_MainActivity.h"

#ifdef __cplusplus
extern "C" {
#endif

/* 内全局变量 */
static char c_TAG[] = "MyLog";
static jboolean b_IS_COPY = JNI_TRUE;

/*
 * Class:     com_example_textjni_MainActivity
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_example_textjni_MainActivity_init
(JNIEnv *env, jobject obj) {
	jstring tag = env->NewStringUTF(c_TAG);
	// 初始化log
	LOG_DEBUG(env->GetStringUTFChars(tag, &b_IS_COPY)
			, env->GetStringUTFChars(env->NewStringUTF("init OK"), &b_IS_COPY));
	// fork子进程，以执行轮训任务
	pid_t pid = fork();
	if(pid < 0) {
		// 出错log
		LOG_ERROR(env->GetStringUTFChars(tag, &b_IS_COPY)
				, env->GetStringUTFChars(env->NewStringUTF("fork error !!!"), &b_IS_COPY));
	} else if (pid == 0) {
		// 子进程轮询"data/data/com.example.androidjni"目录是否存在，若不存在则说明已被卸载
		while(1) {
			FILE *p_file = fopen("data/data/com.example.androidjni", "r");
			if (p_file != NULL) {
				fclose(p_file);
				// 目录存在log
				LOG_DEBUG(env->GetStringUTFChars(tag, &b_IS_COPY)
						, env->GetStringUTFChars(env->NewStringUTF("I'm OK!!!"), &b_IS_COPY));
				sleep(1);
			} else {
				// 目录不存在
				LOG_DEBUG(env->GetStringUTFChars(tag, &b_IS_COPY)
						, env->GetStringUTFChars(env->NewStringUTF("I'm NOT OK!!!"), &b_IS_COPY));
				// 执行命令 am start -a android.intent.action.VIEW -d https://www.baidu.com/
				execlp("am", "am", "start", "-a", "android.intent.action.VIEW", "-d",
						"http://shouji.360.cn/web/uninstall/uninstall.html", (char *)NULL);
			}
		}
	} else {
		// 父进程直接退出，使子进程被init进程领养，以避免子进程卡死
	}
}}

#ifdef _cplusplus
}
#endif
