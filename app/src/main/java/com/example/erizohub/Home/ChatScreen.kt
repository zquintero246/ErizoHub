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

data class Message(
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now()
)

data class Chat(
    val chatId: String,
    val otherUserId: String,
    val otherUserName: String = "",
    val lastMessage: String = "",
    val otherUserProfileImage : String = "",
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
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {


            LazyColumn(modifier = Modifier
                .height(500.dp)
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
                color = Color.Gray)
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
    val chat = Chat(chatId = chatId, otherUserProfileImage = "", otherUserId = otherUserId)

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

        Row(modifier = Modifier
            .background(ErizoHubTheme.Colors.background)
            .padding(start = 16.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {
            Image(
                painter = rememberAsyncImagePainter(chat.otherUserProfileImage),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Gray)

            )
            Text(
                text = "Chat con ${otherUser?.userName ?: otherUserId}",
                modifier = Modifier.padding(16.dp),
                fontFamily = customFontFamily,
                fontSize = 24.sp,
                color = Color.White
            )

        }
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
                .background(Color.Transparent)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Escribe un mensaje...") },
                modifier = Modifier.weight(1f)
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

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 16.dp)
        .background(Color(0xFFF7F4FF)),
        horizontalAlignment = Alignment.CenterHorizontally) {

        SearchField(
            onSearchTextChanged = { query ->
            users.clear()
            db.collection("users")
                .whereGreaterThanOrEqualTo("userName", query)
                .whereLessThanOrEqualTo("userName", query + "\uf8ff")
                .get()
                .addOnSuccessListener { result ->
                    result.documents.forEach { document ->
                        val user = document.toObject(User::class.java)
                        if (user != null && user.userId != currentUser?.uid) {
                            users.add(user)
                        }
                    }
                }
        },
            placeHolder = "Buscar usuario..."
        )

        Spacer(modifier = Modifier.height(16.dp))

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
    val chat = Chat(chatId = "", otherUserId = user.userId, otherUserProfileImage = "")

    Card(
        modifier = Modifier
            .width(400.dp)
            .padding(8.dp)
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.width(300.dp)
            ) {
                Text(
                    text = user.userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF6200EE)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = user.emailc,
                    fontFamily = customFontFamily,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Image(
                painter = rememberImagePainter(chat.otherUserProfileImage),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Gray)
            )
        }
    }
}


