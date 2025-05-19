package com.example.a5046a3.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository class to handle Firebase Authentication and Firestore operations
 */
class FirebaseRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    
    /**
     * Current signed-in Firebase user
     */
    val currentUser: FirebaseUser?
        get() = auth.currentUser
    
    /**
     * Check if user is signed in
     */
    val isSignedIn: Boolean
        get() = auth.currentUser != null
    
    /**
     * Register a new user with email and password
     * 
     * @param email User email
     * @param password User password
     * @param userData Additional user data to store in Firestore
     * @return Result containing user or error
     */
    suspend fun registerUser(
        email: String,
        password: String,
        userData: Map<String, Any>
    ): Result<FirebaseUser> = withContext(Dispatchers.IO) {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user
            
            // If registration successful, store additional user data in Firestore
            if (user != null) {
                // Create a document for the user with their UID as the document ID
                val userDocument = userData.toMutableMap()
                userDocument["email"] = email
                
                firestore.collection("users")
                    .document(user.uid)
                    .set(userDocument)
                    .await()
                
                Result.success(user)
            } else {
                Result.failure(Exception("User registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Sign in a user with email and password
     * 
     * @param email User email
     * @param password User password
     * @return Result containing user or error
     */
    suspend fun signIn(email: String, password: String): Result<FirebaseUser> = 
        withContext(Dispatchers.IO) {
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val user = authResult.user
                
                if (user != null) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("Sign in failed"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    /**
     * Sign out the current user
     */
    fun signOut() {
        auth.signOut()
    }
    
    /**
     * Get user data from Firestore
     * 
     * @param userId User ID
     * @return Result containing user data or error
     */
    suspend fun getUserData(userId: String): Result<Map<String, Any>> = 
        withContext(Dispatchers.IO) {
            try {
                val documentSnapshot = firestore.collection("users")
                    .document(userId)
                    .get()
                    .await()
                
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.data
                    if (userData != null) {
                        Result.success(userData)
                    } else {
                        Result.failure(Exception("User data is null"))
                    }
                } else {
                    Result.failure(Exception("User document does not exist"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    /**
     * Save data to Firestore
     * 
     * @param collection Collection name
     * @param document Document ID (optional, will be auto-generated if null)
     * @param data Data to save
     * @return Result containing document ID or error
     */
    suspend fun saveData(
        collection: String,
        document: String? = null,
        data: Map<String, Any>
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val collectionRef = firestore.collection(collection)
            val documentId = if (document != null) {
                // Use provided document ID
                collectionRef.document(document)
                    .set(data)
                    .await()
                document
            } else {
                // Auto-generate document ID
                val documentRef = collectionRef.add(data).await()
                documentRef.id
            }
            
            Result.success(documentId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get data from Firestore
     * 
     * @param collection Collection name
     * @param document Document ID
     * @return Result containing document data or error
     */
    suspend fun getData(
        collection: String,
        document: String
    ): Result<Map<String, Any>?> = withContext(Dispatchers.IO) {
        try {
            val documentSnapshot = firestore.collection(collection)
                .document(document)
                .get()
                .await()
            
            if (documentSnapshot.exists()) {
                Result.success(documentSnapshot.data)
            } else {
                Result.success(null)  // Document doesn't exist, return null
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Delete data from Firestore
     * 
     * @param collection Collection name
     * @param document Document ID
     * @return Result with success or error
     */
    suspend fun deleteData(
        collection: String,
        document: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            firestore.collection(collection)
                .document(document)
                .delete()
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 