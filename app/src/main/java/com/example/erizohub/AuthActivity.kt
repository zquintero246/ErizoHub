package com.example.erizohub

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore


class AuthActivity : ComponentActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                    composable("login") { IniciarSesion(myController) }
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
        FirebaseApp.initializeApp(this).also { onInitialized() }
    }

    // Launcher para Google Sign-In
    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
                checkIfNewUser(user)
            } else {
                Toast.makeText(this, "Autenticación fallida", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkIfNewUser(user: FirebaseUser?) {
        user?.let {
            val isNewUser = user.metadata?.creationTimestamp == user.metadata?.lastSignInTimestamp
            if (isNewUser) {
                saveUserDetailsToFirestore(user)
            } else {
                Toast.makeText(this, "Este usuario ya está registrado.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun saveUserDetailsToFirestore(user: FirebaseUser) {
        val newUser = hashMapOf(
            "userName" to (user.displayName ?: ""),
            "email" to user.email,
            "profilePictureUrl" to (user.photoUrl?.toString() ?: "")
        )

        db.collection("users").document(user.uid).set(newUser)
            .addOnSuccessListener {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                // Navegar a la pantalla principal
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun signUpWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }
}
