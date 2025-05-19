package com.example.a5046a3.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 身份验证管理器 - 处理Firebase Auth相关功能
 */
class AuthManager {
    private val TAG = "AuthManager"
    
    // Firebase Auth实例
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    
    // 获取当前登录用户
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
    
    // 检查用户是否已登录
    fun isUserLoggedIn(): Boolean = auth.currentUser != null
    
    /**
     * 使用邮箱和密码注册新用户
     */
    suspend fun registerWithEmail(email: String, password: String): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("用户创建失败"))
        } catch (e: Exception) {
            Log.e(TAG, "注册失败: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * 使用邮箱和密码登录
     */
    suspend fun loginWithEmail(email: String, password: String): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("登录失败"))
        } catch (e: Exception) {
            Log.e(TAG, "登录失败: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * 发送密码重置邮件
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "发送密码重置邮件失败: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * 退出登录
     */
    fun signOut() {
        auth.signOut()
    }
    
    /**
     * 删除当前用户账户
     */
    suspend fun deleteAccount(): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            auth.currentUser?.delete()?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "删除账户失败: ${e.message}", e)
            Result.failure(e)
        }
    }
} 