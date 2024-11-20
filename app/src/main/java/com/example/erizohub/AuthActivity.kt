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
import com.example.erizohub.InicioApp.IniciarSesion
import com.example.erizohub.InicioApp.LoadingScreenContent
import com.example.erizohub.InicioApp.PreLogin
import com.example.erizohub.InicioApp.Registrarse
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore

// Clase de actividad para manejar la autenticación
class AuthActivity : ComponentActivity() {
    // Variables para manejar Google Sign-In y Firebase Authentication
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance() // Referencia a Firestore
    private var isSigningUp = false // Indica si el usuario está registrándose

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this) // Inicializa Firebase
        auth = FirebaseAuth.getInstance() // Obtiene la instancia de FirebaseAuth
        super.onCreate(savedInstanceState)

        // Si hay un usuario autenticado, redirige a MainActivity
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Finaliza esta actividad
            return
        }

        // Inicializa Firebase y configura Google Sign-In
        initializeFirebase {
            auth = FirebaseAuth.getInstance()

            // Configuración de opciones de Google Sign-In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // Solicita acceso al correo electrónico del usuario
                .requestScopes(Scope(DriveScopes.DRIVE_FILE)) // Solicita permisos para Drive
                .requestIdToken(getString(R.string.default_web_client_id)) // Solicita el token de ID
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso) // Crea el cliente de Google Sign-In

            // Configura la interfaz con Jetpack Compose
            setContent {
                val myController = rememberNavController() // Controlador de navegación

                // Define las rutas de navegación
                NavHost(
                    navController = myController,
                    startDestination = "loading" // Pantalla inicial
                ) {
                    composable("loading") {
                        LoadingScreenContent(myController) // Pantalla de carga
                    }
                    composable("prelogin") {
                        PreLogin(
                            navController = myController,
                            onButtonClickIniciar = { myController.navigate("login") }, // Navega a "login"
                            onButtonClickRegistrarse = { myController.navigate("register") }, // Navega a "register"
                        )
                    }
                    composable("login") {
                        IniciarSesion(
                            navController = myController,
                            onGoogleSignInClick = { signInWithGoogle() } // Maneja el inicio con Google
                        )
                    }
                    composable("register") {
                        Registrarse(
                            navController = myController,
                            onGoogleSignUpClick = { signUpWithGoogle() } // Maneja el registro con Google
                        )
                    }
                }
            }
        }
    }

    // Inicializa Firebase y ejecuta el bloque proporcionado al completarse
    private fun initializeFirebase(onInitialized: () -> Unit) {
        FirebaseApp.initializeApp(this)?.let {
            onInitialized()
        }
    }

    // Maneja el inicio de sesión con Google
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent) // Lanza la actividad de Google Sign-In
    }

    // Maneja el registro con Google
    private fun signUpWithGoogle() {
        isSigningUp = true // Marca que el usuario está registrándose
        val signUpIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signUpIntent)
    }

    // Resultado de la actividad para Google Sign-In
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data) // Obtiene la cuenta de Google
        try {
            val account = task.getResult(ApiException::class.java) // Maneja posibles errores
            if (account != null) {
                firebaseAuthWithGoogle(account) // Autentica con Firebase usando la cuenta de Google
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "Google Sign-In falló: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Autentica al usuario con Firebase usando las credenciales de Google
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null) // Obtiene las credenciales
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser // Usuario autenticado
                val isNewUser = task.result?.additionalUserInfo?.isNewUser ?: false
                if (isSigningUp && isNewUser) {
                    saveUserDetailsToFirestore(user) // Guarda los datos del usuario en Firestore
                } else {
                    // Redirige a MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "Autenticación fallida: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Guarda los detalles del usuario en Firestore
    private fun saveUserDetailsToFirestore(user: FirebaseUser?) {
        user?.let {
            val newUser = hashMapOf(
                "userId" to user.uid,
                "userName" to (user.displayName ?: ""), // Nombre del usuario
                "email" to user.email, // Correo electrónico
                "profilePictureUrl" to (user.photoUrl?.toString() ?: "") // URL de la foto de perfil
            )

            db.collection("users").document(user.uid).set(newUser) // Almacena los datos en Firestore
                .addOnSuccessListener {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    // Redirige a MainActivity tras un registro exitoso
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