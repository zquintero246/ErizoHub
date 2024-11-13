package com.example.erizohub.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.erizohub.ClasesBD.User
import com.example.erizohub.InicioApp.ErizoHubTheme
import com.example.erizohub.InicioApp.ErizoHubTheme.Fonts.customFontFamily
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Message(
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now()
)

data class Chat(
    val chatId: String,
    val otherUserId: String,
    val lastMessage: String = "",
    val timestamp: Timestamp = Timestamp.now()
)


fun startChat(navController: NavController, currentUserId: String, otherUserId: String) {
    val db = Firebase.firestore

    db.collection("chats")
        .whereArrayContains("participants", currentUserId)
        .get()
        .addOnSuccessListener { result ->
            var chatId: String? = null
            for (document in result.documents) {
                val participants = document.get("participants") as? List<*>
                if (participants != null && otherUserId in participants) {
                    chatId = document.id
                    break
                }
            }

            if (chatId == null) {
                val newChat = hashMapOf(
                    "participants" to listOf(currentUserId, otherUserId),
                    "lastMessage" to "",
                    "timestamp" to Timestamp.now()
                )
                db.collection("chats")
                    .add(newChat)
                    .addOnSuccessListener { newChatDocument ->
                        chatId = newChatDocument.id
                        navController.navigate("chat_screen/$chatId/$otherUserId")
                    }
            } else {
                navController.navigate("chat_screen/$chatId/$otherUserId")
            }
        }
}

@Composable
fun ChatSelectionScreen(navController: NavController) {
    val db = Firebase.firestore
    val user = FirebaseAuth.getInstance().currentUser
    val chatList = remember { mutableStateListOf<Chat>() }

    LaunchedEffect(user) {
        db.collection("chats")
            .whereArrayContains("participants", user?.uid ?: "")
            .addSnapshotListener { snapshot, _ ->
                chatList.clear()
                snapshot?.documents?.forEach { document ->
                    val otherUserId = (document.get("participants") as List<*>).first { it != user?.uid } as String
                    val chat = Chat(
                        chatId = document.id,
                        otherUserId = otherUserId,
                        lastMessage = document.getString("lastMessage") ?: "",
                        timestamp = document.getTimestamp("timestamp") ?: Timestamp.now()
                    )
                    chatList.add(chat)
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ErizoHubTheme.Colors.background)
    ) {

        Column (Modifier
            .fillMaxWidth()
            .background(ErizoHubTheme.Colors.background),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "CHATS",
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = ErizoHubTheme.Fonts.customFontFamily,
                color = Color.White
            )
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
            verticalArrangement = Arrangement.Top) {


            LazyColumn(modifier = Modifier
                .height(550.dp)
                .padding(start = 16.dp, end = 16.dp, top = 26.dp),) {
                items(chatList.size) { index ->
                    val chat = chatList[index]
                    ChatListItem(chat = chat) {
                        navController.navigate("chat_screen/${chat.chatId}/${chat.otherUserId}")
                    }
                }
            }


            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = { navController.navigate("user_selection_screen") },
                    colors = ButtonDefaults.buttonColors(containerColor = ErizoHubTheme.Colors.primary, contentColor = Color.Gray) ,
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .width(75.dp)
                        .height(75.dp)
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

@Composable
fun ChatListItem(chat: Chat, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            Text(
                text = chat.otherUserId,
                fontSize = 18.sp,
                color = Color(0xFF6200EE)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = chat.lastMessage, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatId: String, otherUserId: String) {
    val db: FirebaseFirestore = Firebase.firestore
    val user = FirebaseAuth.getInstance().currentUser
    val messages = remember { mutableStateListOf<Message>() }
    var messageText by remember { mutableStateOf("") }
    var otherUser by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(otherUserId) {
        db.collection("users").document(otherUserId).get().addOnSuccessListener { document ->
            otherUser = document.toObject(User::class.java)
        }
    }

    LaunchedEffect(chatId) {
        db.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                messages.clear()
                snapshot?.documents?.forEach { document ->
                    val message = Message(
                        senderId = document.getString("senderId") ?: "",
                        receiverId = document.getString("receiverId") ?: "",
                        text = document.getString("text") ?: "",
                        timestamp = document.getTimestamp("timestamp") ?: Timestamp.now()
                    )
                    messages.add(message)
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF7F4FF))) {
        Text(
            text = "Chat con ${otherUser?.userName ?: otherUserId}",
            modifier = Modifier.padding(16.dp),
            fontSize = 24.sp,
            color = Color(0xFF6200EE)
        )

        LazyColumn(
            modifier = Modifier.weight(1f).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages.size) { index ->
                val message = messages[index]
                ChatBubble(
                    message = message,
                    isSentByCurrentUser = message.senderId == user?.uid
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White, RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Escribe un mensaje...") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent
                )
            )
            IconButton(onClick = {
                if (messageText.isNotBlank()) {
                    val newMessage = hashMapOf(
                        "senderId" to user?.uid,
                        "receiverId" to otherUserId,
                        "text" to messageText,
                        "timestamp" to Timestamp.now()
                    )
                    db.collection("chats").document(chatId).collection("messages")
                        .add(newMessage)
                    db.collection("chats").document(chatId)
                        .update("lastMessage", messageText, "timestamp", Timestamp.now())
                    messageText = ""
                }
            }) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Enviar mensaje")
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message, isSentByCurrentUser: Boolean) {
    Box(
        contentAlignment = if (isSentByCurrentUser) Alignment.CenterEnd else Alignment.CenterStart,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = message.text,
            color = if (isSentByCurrentUser) Color.White else Color.Black,
            modifier = Modifier
                .background(
                    color = if (isSentByCurrentUser) Color(0xFF6200EE) else Color(0xFFE1E1E1),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(8.dp)
        )
    }
}

@Composable
fun UserSelectionScreen(navController: NavController) {
    val db = Firebase.firestore
    val users = remember { mutableStateListOf<User>() }
    val currentUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(Unit) {
        db.collection("users").get().addOnSuccessListener { result ->
            users.clear()
            result.documents.forEach { document ->
                val user = document.toObject(User::class.java)
                if (user != null && user.userId != currentUser?.uid) {
                    users.add(user)
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF7F4FF))) {
        Text(
            text = "Selecciona un usuario para chatear",
            modifier = Modifier.padding(16.dp),
            fontSize = 24.sp,
            color = Color(0xFF6200EE)
        )

        LazyColumn {
            items(users.size) { index ->
                val user = users[index]
                UserListItem(user = user) {
                    startChat(navController, currentUser?.uid ?: "", user.userId)
                }
            }
        }
    }
}

@Composable
fun UserListItem(user: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
            .background(Color(0xFFF2E7FE)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = user.userName,
                fontSize = 18.sp,
                color = Color(0xFF6200EE)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = user.emailc,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}


