package com.mitnick.tannotour.easylib

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mitnick.tannotour.easylib.cache.Cache
import com.mitnick.tannotour.easylib.cache.CacheObserver
import com.mitnick.tannotour.easylib.cache.disk.Disk
import com.mitnick.tannotour.easylib.lifecycle.ActivityLifecycleCallbacksCompat
import com.mitnick.tannotour.easylib.lifecycle.FragmentLifecycleCallbacks
import com.mitnick.tannotour.easylib.lifecycle.LifecycleDispatcher
import java.lang.ref.WeakReference

/**
 * Created by mitnick on 2017/10/9.
 * Description 初始化框架
 */
object LibInit {

    val TAG = "LibInit"
    var applicaion: WeakReference<Application>? = null

    fun appInit(app: Application){
        if(null == applicaion){
            applicaion = WeakReference(app)
            Cache.init(app.applicationContext)
            registerLifecycle(app)
        }
    }

    fun registerLifecycle(app: Application){
        LifecycleDispatcher.registerLifecycleCallbacks(
                app,
                object : ActivityLifecycleCallbacksCompat {
                    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                        if(activity!=null && activity is CacheObserver && Cache.addObserver(activity)){
                            Log.e(TAG, activity.localClassName + "自动执行Cache监听初始化完毕。")
                        }else{
                            Log.e(TAG, activity?.localClassName + "跳过Cache初始化。")
                        }
                    }

                    override fun onActivityStarted(activity: Activity?) {
                        
                    }

                    override fun onActivityResumed(activity: Activity?) {

                    }

                    override fun onActivityPaused(activity: Activity?) {
//                        if(activity!=null && activity is CacheObserver && Cache.addObserver(activity)){
//                            Cache.flush()
//                            Log.e(TAG, activity.localClassName + "自动执行同步Cache到硬盘完成")
//                        }
                        if(activity!=null && activity is CacheObserver){
                            Cache.flush()
                            Log.e(TAG, activity.localClassName + "自动执行同步Cache到硬盘完成")
                        }
                    }

                    override fun onActivityStopped(activity: Activity?) {

                    }

                    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

                    }

                    override fun onActivityDestroyed(activity: Activity?) {
                        if(activity!=null && activity is CacheObserver && Cache.removeObserver(activity)){
                            Log.e(TAG, activity.localClassName + "自动执行Cache释放监听初始化完毕。")
                        }else{
                            Log.e(TAG, activity?.localClassName + "跳过执行Cache释放监听。")
                        }
                    }
                },
                object : FragmentLifecycleCallbacks {
                    override fun onFragmentCreated(fragment: Fragment?, savedInstanceState: Bundle?) {

                    }

                    override fun onFragmentStarted(fragment: Fragment?) {

                    }

                    override fun onFragmentResumed(fragment: Fragment?) {

                    }

                    override fun onFragmentPaused(fragment: Fragment?) {

                    }

                    override fun onFragmentStopped(fragment: Fragment?) {

                    }

                    override fun onFragmentSaveInstanceState(fragment: Fragment?, outState: Bundle?) {

                    }

                    override fun onFragmentDestroyed(fragment: Fragment?) {
                        if(fragment!=null && fragment is CacheObserver && Cache.removeObserver(fragment)){
                            Log.e(TAG, fragment.javaClass.name + "自动执行Cache释放监听初始化完毕。")
                        }else{
                            Log.e(TAG, fragment?.javaClass?.name + "跳过执行Cache释放监听。")
                        }
                    }

                    override fun onFragmentAttach(fragment: Fragment?, activity: Activity?) {

                    }

                    override fun onFragmentDetach(fragment: Fragment?) {

                    }

                    override fun onFragmentActivityCreated(fragment: Fragment?, savedInstanceState: Bundle?) {

                    }

                    override fun onFragmentCreateView(fragment: Fragment?, inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) {

                    }

                    override fun onFragmentViewCreated(fragment: Fragment?, view: View?, savedInstanceState: Bundle?) {
                        if(fragment!=null && fragment is CacheObserver && Cache.addObserver(fragment)){
                            Log.e(TAG, fragment.javaClass.name + "自动执行Cache监听初始化完毕。")
                        }else{
                            Log.e(TAG, fragment?.javaClass?.name + "跳过Cache初始化。")
                        }
                    }

                }
        )
    }
}