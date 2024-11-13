package com.example.erizohub

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AuthActivity : ComponentActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private var isSigningUp = false

    override fun onCreate(savedInstanceState: Bundle?) {

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        initializeFirebase {
            auth = FirebaseAuth.getInstance()

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)

            setContent {
                val myController = rememberNavController()
                NavHost(
                    navController = myController,
                    startDestination = "loading"
                ) {
                    composable("loading") { LoadingScreenContent(myController) }
                    composable("prelogin") {
                        PreLogin(
                            navController = myController,
                            onButtonClickIniciar = { myController.navigate("login") },
                            onButtonClickRegistrarse = { myController.navigate("register") },
                        )
                    }
                    composable("login") {
                        IniciarSesion(
                            navController = myController,
                            onGoogleSignInClick = { signInWithGoogle() }
                        )
                    }
                    composable("register") {
                        Registrarse(
                            navController = myController,
                            onGoogleSignUpClick = { signUpWithGoogle() }
                        )
                    }
                    composable("profile") {
                        Perfil(navController = myController)
                    }
                }
            }
        }
    }

    private fun initializeFirebase(onInitialized: () -> Unit) {
        FirebaseApp.initializeApp(this)?.let {
            onInitialized()
        }
    }


    private fun signUpWithGoogle() {
        isSigningUp = true
        val signUpIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signUpIntent)
    }

    private fun signInWithGoogle() {
        isSigningUp = false
        lifecycleScope.launch {
            try {
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            } catch (e: Exception) {
                Toast.makeText(this@AuthActivity, "Error al iniciar sesión con Google", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            Toast.makeText(this, "Google Sign-In falló: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val isNewUser = task.result?.additionalUserInfo?.isNewUser ?: false
                if (isSigningUp && isNewUser) {
                    saveUserDetailsToFirestore(user)
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "Autenticación fallida: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserDetailsToFirestore(user: FirebaseUser?) {
        user?.let {
            val newUser = hashMapOf(
                "userId" to user.uid,
                "userName" to (user.displayName ?: ""),
                "email" to user.email,
                "profilePictureUrl" to (user.photoUrl?.toString() ?: "")
            )

            db.collection("users").document(user.uid).set(newUser)
                .addOnSuccessListener {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al guardar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
