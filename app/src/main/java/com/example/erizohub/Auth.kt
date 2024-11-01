import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

val auth: FirebaseAuth = FirebaseAuth.getInstance()
val db = FirebaseFirestore.getInstance()

fun registerUser(email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
            } else {
                // Error en el registro
            }
        }
}

fun loginUser(email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
            } else {
                // Error en el inicio de sesión
            }
        }
}





fun saveUserData(userId: String, username: String, email: String) {
    val user = hashMapOf(
        "username" to username,
        "email" to email
    )

    db.collection("users").document(userId)
        .set(user)
        .addOnSuccessListener {
            // Datos guardados correctamente
        }
        .addOnFailureListener { e ->
            // Error al guardar datos
        }
}