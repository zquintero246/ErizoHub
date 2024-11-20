package com.example.erizohub.Home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import coil.compose.rememberImagePainter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.erizohub.ClasesBD.User
import com.example.erizohub.InicioApp.ErizoHubTheme
import com.example.erizohub.InicioApp.ErizoHubTheme.Fonts.customFontFamily
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Modelo de datos para un mensaje en el chat.
data class Message(
    val senderId: String = "", // ID del remitente.
    val receiverId: String = "", // ID del receptor.
    val text: String = "", // Contenido del mensaje.
    val timestamp: Timestamp = Timestamp.now() // Marca de tiempo del mensaje.
)

// Modelo de datos para un chat.
data class Chat(
    val chatId: String, // ID único del chat.
    val otherUserId: String, // ID del otro usuario en el chat.
    val otherUserName: String = "", // Nombre del otro usuario.
    val lastMessage: String = "", // Último mensaje en el chat.
    val otherUserProfileImage: String = "", // Imagen de perfil del otro usuario.
    val timestamp: Timestamp = Timestamp.now() // Marca de tiempo del último mensaje.
)

// Función para iniciar un nuevo chat o navegar a uno existente.
fun startChat(navController: NavController, currentUserId: String, otherUserId: String) {
    val db = Firebase.firestore // Referencia a la base de datos Firestore.

    db.collection("chats")
        .whereArrayContains("participants", currentUserId) // Busca chats donde participa el usuario actual.
        .get()
        .addOnSuccessListener { result ->
            var chatId: String? = null
            // Verifica si ya existe un chat entre los participantes.
            for (document in result.documents) {
                val participants = document.get("participants") as? List<*>
                if (participants != null && otherUserId in participants) {
                    chatId = document.id // Obtiene el ID del chat existente.
                    break
                }
            }

            if (chatId == null) { // Si no existe el chat, se crea uno nuevo.
                val newChat = hashMapOf(
                    "participants" to listOf(currentUserId, otherUserId),
                    "lastMessage" to "",
                    "timestamp" to Timestamp.now()
                )
                db.collection("chats")
                    .add(newChat)
                    .addOnSuccessListener { newChatDocument ->
                        chatId = newChatDocument.id // Guarda el ID del nuevo chat.
                        navController.navigate("chat_screen/$chatId/$otherUserId") // Navega al nuevo chat.
                    }
            } else {
                navController.navigate("chat_screen/$chatId/$otherUserId") // Navega al chat existente.
            }
        }
}

// Pantalla que muestra una lista de chats del usuario actual.
@Composable
fun ChatSelectionScreen(navController: NavController) {
    val db = Firebase.firestore
    val user = FirebaseAuth.getInstance().currentUser
    val chatList = remember { mutableStateListOf<Chat>() } // Lista de chats observables.

    LaunchedEffect(user) { // Carga los chats del usuario al iniciar la pantalla.
        db.collection("chats")
            .whereArrayContains("participants", user?.uid ?: "")
            .addSnapshotListener { snapshot, _ ->
                chatList.clear()
                snapshot?.documents?.forEach { document ->
                    val otherUserId = (document.get("participants") as List<*>).first { it != user?.uid } as String
                    db.collection("users").document(otherUserId).get()
                        .addOnSuccessListener { otherUserDoc ->
                            val otherUserName = otherUserDoc.getString("userName") ?: ""
                            val otherUserProfileImage = otherUserDoc.getString("profileImage") ?: ""
                            val chat = Chat(
                                chatId = document.id,
                                otherUserName = otherUserName,
                                otherUserProfileImage = otherUserProfileImage,
                                otherUserId = otherUserId,
                                lastMessage = document.getString("lastMessage") ?: "",
                                timestamp = document.getTimestamp("timestamp") ?: Timestamp.now()
                            )
                            chatList.add(chat)
                        }
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ErizoHubTheme.Colors.background)
    ) {
        // Título de la pantalla.
        Column(
            Modifier
                .fillMaxWidth()
                .background(ErizoHubTheme.Colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CHATS",
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                color = Color.White
            )
        }

        // Lista de chats.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .height(500.dp)
                    .padding(start = 16.dp, end = 16.dp, top = 26.dp)
            ) {
                items(chatList.size) { index ->
                    val chat = chatList[index]
                    ChatListItem(chat = chat) {
                        navController.navigate("chat_screen/${chat.chatId}/${chat.otherUserId}")
                    }
                }
            }

            // Botón para iniciar un nuevo chat.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { navController.navigate("user_selection_screen") },
                    colors = ButtonDefaults.buttonColors(containerColor = ErizoHubTheme.Colors.primary, contentColor = Color.Gray),
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(60.dp)
                        .border(1.dp, Color.Transparent)
                        .shadow(10.dp, shape = RoundedCornerShape(30.dp)),
                ) {
                    Image(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Enviar mensaje",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}

// Elemento individual de la lista de chats.
@Composable
fun ChatListItem(chat: Chat, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(350.dp)
            .padding(8.dp)
            .clickable(onClick = onClick)
            .shadow(8.dp, shape = RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color(0xFFE1E1E1)),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White,
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = chat.otherUserName,
                fontFamily = customFontFamily,
                fontSize = 18.sp,
                color = ErizoHubTheme.Colors.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier.alpha(0.6f),
                text = chat.lastMessage,
                fontSize = 14.sp,
                fontFamily = customFontFamily,
                color = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatId: String, otherUserId: String) {
    val db: FirebaseFirestore = Firebase.firestore // Inicializa Firestore.
    val user = FirebaseAuth.getInstance().currentUser // Obtiene el usuario autenticado.
    val messages = remember { mutableStateListOf<Message>() } // Lista observable de mensajes.
    var messageText by remember { mutableStateOf("") } // Texto del mensaje actual.
    var otherUser by remember { mutableStateOf<User?>(null) } // Información del otro usuario en el chat.
    val chat = Chat(chatId = chatId, otherUserProfileImage = "", otherUserId = otherUserId) // Datos del chat actual.

    // Carga la información del otro usuario al inicio.
    LaunchedEffect(otherUserId) {
        db.collection("users").document(otherUserId).get().addOnSuccessListener { document ->
            otherUser = document.toObject(User::class.java) // Convierte el documento en un objeto User.
        }
    }

    // Escucha los mensajes del chat en tiempo real.
    LaunchedEffect(chatId) {
        db.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp") // Ordena los mensajes por la marca de tiempo.
            .addSnapshotListener { snapshot, _ ->
                messages.clear() // Limpia la lista de mensajes antes de actualizar.
                snapshot?.documents?.forEach { document ->
                    val message = Message(
                        senderId = document.getString("senderId") ?: "",
                        receiverId = document.getString("receiverId") ?: "",
                        text = document.getString("text") ?: "",
                        timestamp = document.getTimestamp("timestamp") ?: Timestamp.now()
                    )
                    messages.add(message) // Agrega cada mensaje a la lista.
                }
            }
    }

    // Estructura de la pantalla.
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF7F4FF))) {

        // Encabezado del chat con el nombre y foto del otro usuario.
        Row(
            modifier = Modifier
                .background(ErizoHubTheme.Colors.background)
                .padding(start = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = rememberAsyncImagePainter(chat.otherUserProfileImage), // Imagen del perfil.
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Gray)
            )
            Text(
                text = "Chat con ${otherUser?.userName ?: otherUserId}", // Muestra el nombre del usuario o el ID.
                modifier = Modifier.padding(16.dp),
                fontFamily = customFontFamily,
                fontSize = 24.sp,
                color = Color.White
            )
        }

        // Lista de mensajes.
        LazyColumn(
            modifier = Modifier.weight(1f).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages.size) { index ->
                val message = messages[index]
                ChatBubble(
                    message = message,
                    isSentByCurrentUser = message.senderId == user?.uid // Verifica si el mensaje fue enviado por el usuario actual.
                )
            }
        }

        // Campo de texto para escribir y enviar mensajes.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = messageText, // Contenido del campo de texto.
                onValueChange = { messageText = it }, // Actualiza el texto del mensaje.
                placeholder = { Text("Escribe un mensaje...") }, // Texto de ayuda en el campo.
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color.Gray, RoundedCornerShape(30.dp))
                    .background(Color.White, RoundedCornerShape(30.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )
            IconButton(
                modifier = Modifier
                    .size(60.dp)
                    .background(color = (ErizoHubTheme.Colors.primary), RoundedCornerShape(30.dp))
                    .clip(RoundedCornerShape(16.dp)),
                onClick = {
                    if (messageText.isNotBlank()) { // Verifica que el texto no esté vacío.
                        val newMessage = hashMapOf(
                            "senderId" to user?.uid,
                            "receiverId" to otherUserId,
                            "text" to messageText,
                            "timestamp" to Timestamp.now()
                        )
                        db.collection("chats").document(chatId).collection("messages")
                            .add(newMessage) // Agrega el mensaje a Firestore.
                        db.collection("chats").document(chatId)
                            .update("lastMessage", messageText, "timestamp", Timestamp.now()) // Actualiza el último mensaje del chat.
                        messageText = "" // Limpia el campo de texto después de enviar.
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Enviar mensaje") // Botón para enviar el mensaje.
            }
        }
    }
}


@Composable
fun ChatBubble(message: Message, isSentByCurrentUser: Boolean) {
    // Define un contenedor para la burbuja del mensaje.
    Box(
        // Alinea el contenido dependiendo de quién envió el mensaje:
        // - Si es enviado por el usuario actual, se alinea a la derecha.
        // - Si es recibido, se alinea a la izquierda.
        contentAlignment = if (isSentByCurrentUser) Alignment.CenterEnd else Alignment.CenterStart,
        modifier = Modifier.fillMaxWidth() // La burbuja ocupa el ancho completo del contenedor.
    ) {
        // Muestra el texto del mensaje dentro de una burbuja.
        Text(
            text = message.text, // Contenido del mensaje.
            color = if (isSentByCurrentUser) Color.White else Color.Black, // Color del texto: blanco si es enviado, negro si es recibido.
            modifier = Modifier
                .background(
                    // Color de fondo de la burbuja:
                    // - Morado para mensajes enviados.
                    // - Gris para mensajes recibidos.
                    color = if (isSentByCurrentUser) Color(0xFF6200EE) else Color(0xFFE1E1E1),
                    shape = RoundedCornerShape(16.dp) // Esquinas redondeadas de la burbuja.
                )
                .padding(8.dp) // Espaciado interno del texto dentro de la burbuja.
        )
    }
}


@Composable
fun UserSelectionScreen(navController: NavController) {
    // Inicializa Firestore y las listas observables de usuarios.
    val db = Firebase.firestore
    val users = remember { mutableStateListOf<User>() } // Lista mutable de usuarios a mostrar.
    val currentUser = FirebaseAuth.getInstance().currentUser // Usuario autenticado.

    // Carga los usuarios desde Firestore al iniciarse la pantalla.
    LaunchedEffect(Unit) {
        db.collection("users").get().addOnSuccessListener { result ->
            users.clear() // Limpia la lista para evitar duplicados.
            result.documents.forEach { document ->
                val user = document.toObject(User::class.java) // Convierte el documento en un objeto `User`.
                if (user != null && user.userId != currentUser?.uid) { // Evita agregar el usuario actual.
                    users.add(user) // Agrega el usuario a la lista.
                }
            }
        }
    }

    // Estructura principal de la pantalla.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .background(Color(0xFFF7F4FF)), // Fondo de la pantalla.
        horizontalAlignment = Alignment.CenterHorizontally // Alineación horizontal central.
    ) {

        // Campo de búsqueda para filtrar usuarios.
        SearchField(
            onSearchTextChanged = { query ->
                users.clear() // Limpia la lista de usuarios al iniciar una nueva búsqueda.
                db.collection("users")
                    .whereGreaterThanOrEqualTo("userName", query) // Filtra los usuarios cuyo nombre empieza con el texto ingresado.
                    .whereLessThanOrEqualTo("userName", query + "\uf8ff")
                    .get()
                    .addOnSuccessListener { result ->
                        result.documents.forEach { document ->
                            val user = document.toObject(User::class.java)
                            if (user != null && user.userId != currentUser?.uid) { // Evita incluir el usuario actual.
                                users.add(user) // Agrega los resultados filtrados.
                            }
                        }
                    }
            },
            placeHolder = "Buscar usuario..." // Texto de ayuda en el campo de búsqueda.
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espaciador entre el campo de búsqueda y la lista.

        // Lista de usuarios filtrados.
        LazyColumn {
            items(users.size) { index ->
                val user = users[index]
                // Cada usuario se muestra con un componente `UserListItem`.
                UserListItem(user = user) {
                    // Al hacer clic en un usuario, se inicia un chat con él.
                    startChat(navController, currentUser?.uid ?: "", user.userId)
                }
            }
        }
    }
}


@Composable
fun UserListItem(user: User, onClick: () -> Unit) {
    // Crea un objeto `Chat` con información básica del usuario, aunque el `chatId` se deja vacío.
    val chat = Chat(chatId = "", otherUserId = user.userId, otherUserProfileImage = "")

    // Contenedor en forma de tarjeta para representar cada usuario.
    Card(
        modifier = Modifier
            .width(400.dp) // Ancho de la tarjeta.
            .padding(8.dp) // Espaciado externo alrededor de la tarjeta.
            .clickable(onClick = onClick) // Hace que toda la tarjeta sea clickeable.
            .clip(RoundedCornerShape(16.dp)), // Bordes redondeados.
        shape = RoundedCornerShape(16.dp) // Forma de la tarjeta.
    ) {
        // Diseño interno de la tarjeta: una fila horizontal.
        Row(
            modifier = Modifier.padding(16.dp), // Espaciado interno de la fila.
            verticalAlignment = Alignment.CenterVertically // Alinea el contenido verticalmente al centro.
        ) {
            // Columna para mostrar el nombre y el email del usuario.
            Column(
                verticalArrangement = Arrangement.Center, // Alineación vertical al centro.
                modifier = Modifier.width(300.dp) // Limita el ancho de la columna.
            ) {
                // Texto para mostrar el nombre del usuario en negrita.
                Text(
                    text = user.userName, // Nombre del usuario.
                    fontWeight = FontWeight.Bold, // Fuente en negrita.
                    fontSize = 18.sp, // Tamaño del texto.
                    color = Color(0xFF6200EE) // Color morado para destacar.
                )
                Spacer(modifier = Modifier.width(8.dp)) // Espaciador entre el nombre y el email.

                // Texto para mostrar el email del usuario.
                Text(
                    text = user.emailc, // Email del usuario.
                    fontFamily = customFontFamily, // Fuente personalizada.
                    fontSize = 14.sp, // Tamaño del texto.
                    color = Color.Gray // Color gris para menor énfasis.
                )
            }

            // Imagen del perfil del usuario.
            Image(
                painter = rememberImagePainter(chat.otherUserProfileImage), // Imagen del perfil.
                contentDescription = "Foto de perfil", // Descripción de la imagen para accesibilidad.
                modifier = Modifier
                    .size(40.dp) // Tamaño de la imagen.
                    .clip(RoundedCornerShape(20.dp)) // Bordes redondeados para simular un círculo.
                    .background(Color.Gray) // Fondo gris por si la imagen no carga.
            )
        }
    }
}



