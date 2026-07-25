package com.algorithmicsluque.miremotito.ui.setup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.carousel.CarouselItemScope
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.algorithmicsluque.miremotito.ui.components.ExpressiveListItem
import com.algorithmicsluque.miremotito.ui.components.IconSelector
import com.algorithmicsluque.miremotito.ui.components.ListItemPosition
import com.algorithmicsluque.miremotito.ui.components.RemotitoAppBar
import com.algorithmicsluque.miremotito.ui.theme.FlowerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceScreen(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    filteredBrands: List<BrandInfo>,
    filteredDeviceTypes: List<DeviceTypeInfo>,
    onTypeSelected: (com.algorithmicsluque.miremotito.data.models.DeviceType) -> Unit,
    onBrandSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            RemotitoAppBar(title = "Nuevo control", onBackClick = onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = PaddingValues(start = 46.dp, end = 46.dp, top = 20.dp, bottom = 120.dp)
        ) {
            item {
                Text(
                    text = "¿De qué tipo de dispositivo querés el control remoto?",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            item {
                Column {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChanged,
                        placeholder = { Text("Tipo de dispositivo o marca") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        trailingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                        singleLine = true
                    )
                    
                    if (searchQuery.isNotEmpty() && filteredBrands.isEmpty() && filteredDeviceTypes.isEmpty()) {
                        Text(
                            text = "No encontramos resultados para \"$searchQuery\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                        )
                    }
                }
            }
            
            if (filteredBrands.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = "MARCAS POPULARES",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        
                        HorizontalMultiBrowseCarousel(
                            state = rememberCarouselState { filteredBrands.size },
                            preferredItemWidth = 180.dp,
                            itemSpacing = 12.dp,
                            contentPadding = PaddingValues(horizontal = 0.dp),
                            modifier = Modifier.fillMaxWidth().height(120.dp)
                        ) { index ->
                            val brand = filteredBrands[index]
                            val containerColors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer,
                                MaterialTheme.colorScheme.tertiaryContainer
                            )
                            BrandCard(
                                name = brand.name,
                                icon = brand.icon,
                                imageRes = brand.imageRes,
                                color = containerColors[index % containerColors.size],
                                onClick = { onBrandSelected(brand.name) }
                            )
                        }
                    }
                }
            }
            
            item {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "DISPOSITIVOS",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        filteredDeviceTypes.forEachIndexed { index, deviceTypeInfo ->
                            val position = when (index) {
                                0 -> if (filteredDeviceTypes.size == 1) ListItemPosition.SINGLE else ListItemPosition.FIRST
                                filteredDeviceTypes.size - 1 -> ListItemPosition.LAST
                                else -> ListItemPosition.MIDDLE
                            }
                            ExpressiveListItem(
                                position = position,
                                onClick = { onTypeSelected(deviceTypeInfo.type) }
                            ) {
                                Icon(imageVector = deviceTypeInfo.icon, contentDescription = null, modifier = Modifier.size(42.dp))
                                Text(text = deviceTypeInfo.label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarouselItemScope.BrandCard(
    name: String,
    icon: ImageVector? = null,
    imageRes: Int? = null,
    color: Color,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(28.dp)

    Card(
        modifier = Modifier
            .fillMaxSize()
            .maskClip(shape = cardShape)
            .clickable { onClick() },
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            if (imageRes != null) {
                AsyncImage(
                    model = imageRes,
                    contentDescription = name,
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                } else {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun BrandsScreen(
    onBrandSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    val brands = listOf("AOC", "BGH", "Hisense", "LG", "Noblex", "Panasonic", "Philips", "Samsung", "Sony", "TCL", "Vizio")
    
    Scaffold(
        topBar = {
            RemotitoAppBar(title = "Marcas", onBackClick = onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            items(brands) { brand ->
                ListItem(
                    headlineContent = { Text(brand, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.clickable { onBrandSelected(brand) }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
fun ErrorScreen(
    onRetry: () -> Unit,
    onCancel: () -> Unit
) {
    Scaffold(
        topBar = {
            RemotitoAppBar(title = "Error al conectar", onBackClick = onCancel)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(46.dp)
        ) {
            Text(
                text = "Hubo un error al conectar con nuestro servidor",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "No te preocupes, no es tu culpa. Volvé a intentarlo.",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp)
            )
            
            Spacer(Modifier.height(100.dp))
            
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Error 500 Internal Server Error.",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(Modifier.weight(1f))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = onRetry,
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Text("Intentar de nuevo")
                }
            }
        }
    }
}

@Composable
fun TestingScreen(
    onPowerTest: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            RemotitoAppBar(title = "Configuración", onBackClick = onBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(46.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Apuntá la Raspberry Pi a la TV y presioná el botón de abajo",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.weight(1f))
            
            Surface(
                modifier = Modifier.size(136.dp).clickable { onPowerTest() },
                shape = RoundedCornerShape(30.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.PowerSettingsNew, contentDescription = null, modifier = Modifier.size(64.dp))
                }
            }
            
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
fun ConfirmationScreen(
    onYes: () -> Unit,
    onNo: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            RemotitoAppBar(title = "Configuración", onBackClick = onBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(46.dp)
        ) {
            Text(
                text = "Apuntá la Raspberry Pi a la TV y presioná el botón de abajo",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "¿Se apagó la TV?",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp)
            )
            
            Spacer(Modifier.weight(1f))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onNo,
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Text("No")
                }
                Button(
                    onClick = onYes,
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Text("Sí")
                }
            }
        }
    }
}

@Composable
fun SuggestRemoteConfirmationScreen(
    onAccept: () -> Unit,
    onCancel: () -> Unit
) {
    Scaffold(
        topBar = {
            RemotitoAppBar(title = "Sugerir nuevo c...", onBackClick = onCancel)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(46.dp)
        ) {
            Text(
                text = "Parece que no tenemos ese control...",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Completá los siguientes datos y envialos para que los podamos revisar.",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp)
            )
            
            Spacer(Modifier.weight(1f))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Icon(Icons.Rounded.Close, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cancelar")
                }
                Button(
                    onClick = onAccept,
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Aceptar")
                }
            }
        }
    }
}

@Composable
fun SuggestRemoteFormScreen(
    state: SuggestUiState,
    onCategoryChanged: (String) -> Unit,
    onFieldChanged: (String, String) -> Unit,
    onAttachPhoto: (String) -> Unit,
    onSend: () -> Unit,
    onBack: () -> Unit
) {
    var showAttachSheet by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            RemotitoAppBar(title = "Sugerir nuevo c...", onBackClick = onBack)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 48.dp)
        ) {
            item {
                Text(
                    text = "Completá estos datos:",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("TV", "AC", "Otros").forEach { category ->
                        val isSelected = state.selectedCategory == category
                        Button(
                            onClick = { onCategoryChanged(category) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(category)
                        }
                    }
                }
            }

            if (state.selectedCategory != "Otros") {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = state.brand,
                            onValueChange = { onFieldChanged("brand", it) },
                            label = { Text("Marca") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = state.model,
                            onValueChange = { onFieldChanged("model", it) },
                            label = { Text("Modelo") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = state.remoteModel,
                            onValueChange = { onFieldChanged("remoteModel", it) },
                            label = { Text("Modelo del control remoto") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = state.specialButtons,
                            onValueChange = { onFieldChanged("specialButtons", it) },
                            label = { Text(if (state.selectedCategory == "AC") "Botones especiales (Turbo, etc.)" else "Botones especiales (Netflix, etc.)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AttachBox(
                        title = "Foto de etiqueta trasera del dispositivo",
                        isAttached = state.hasBackPhoto,
                        modifier = Modifier.weight(1f),
                        onClick = { showAttachSheet = "back" }
                    )
                    AttachBox(
                        title = "Foto del control remoto original",
                        isAttached = state.hasRemotePhoto,
                        modifier = Modifier.weight(1f),
                        onClick = { showAttachSheet = "remote" }
                    )
                }
            }

            if (state.selectedCategory == "Otros") {
                item {
                    AttachBox(
                        title = "Foto frontal del dispositivo",
                        isAttached = state.hasFrontPhoto,
                        modifier = Modifier.fillMaxWidth().height(145.dp),
                        onClick = { showAttachSheet = "front" }
                    )
                }
            }

            item {
                Button(
                    onClick = onSend,
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
                ) {
                    Icon(Icons.AutoMirrored.Rounded.Send, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text(text = "Enviar", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }

    if (showAttachSheet != null) {
        ImageAttachmentSheet(
            type = showAttachSheet!!,
            onAttach = {
                onAttachPhoto(showAttachSheet!!)
                showAttachSheet = null
            },
            onDismiss = { showAttachSheet = null }
        )
    }
}

@Composable
fun AttachBox(title: String, isAttached: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.height(145.dp).clickable { onClick() },
        color = if (isAttached) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isAttached) Icons.Rounded.Check else Icons.Rounded.SwapHoriz,
                contentDescription = null
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            if (isAttached) {
                Text(
                    text = "Adjuntada",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageAttachmentSheet(type: String, onAttach: () -> Unit, onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp).padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Adjuntar imágenes", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                text = when(type) {
                    "back" -> "Foto de etiqueta trasera del dispositivo"
                    "remote" -> "Foto del control remoto original"
                    else -> "Foto del frente o etiqueta frontal del dispositivo"
                },
                style = MaterialTheme.typography.titleMedium
            )
            
            Surface(
                modifier = Modifier.fillMaxWidth().height(200.dp).clickable { onAttach() },
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(28.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(Icons.Rounded.Image, contentDescription = null, modifier = Modifier.size(48.dp))
                    Text(text = "Adjuntar imagen", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        text = "Subí una foto de 800 px y 10 KB como mínimo, y hasta 4000 px o 10 MB como máximo.",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun PersonalizationScreen(
    name: String,
    onNameChange: (String) -> Unit,
    selectedIcon: ImageVector,
    onIconSelected: (ImageVector) -> Unit,
    onAdd: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            RemotitoAppBar(title = "Nuevo control", onBackClick = onBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(46.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = RoundedCornerShape(50.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = selectedIcon, contentDescription = null, modifier = Modifier.size(50.dp), tint = MaterialTheme.colorScheme.primaryContainer)
                    }
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Nombre del dispositivo") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            IconSelector(onIconSelected = onIconSelected)
            
            Spacer(Modifier.weight(1f))
            
            Button(
                onClick = onAdd,
                modifier = Modifier.align(Alignment.CenterHorizontally).width(150.dp).height(56.dp)
            ) {
                Icon(Icons.Rounded.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Añadir")
            }
        }
    }
}

@Composable
fun SuccessScreen(
    onGoHome: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(46.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            Surface(
                modifier = Modifier.size(252.dp),
                shape = FlowerShape,
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = Color.White
                    )
                }
            }
            
            Text(
                text = "¡Ya está!",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black
            )
            
            Button(
                onClick = onGoHome,
                modifier = Modifier.width(238.dp).height(96.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
            ) {
                Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text(text = "Ir a Inicio", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGroupSheet(
    newGroupName: String,
    defaultGroups: List<String>,
    onGroupNameChanged: (String) -> Unit,
    onAddGroup: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        contentWindowInsets = { WindowInsets(0.dp) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .imePadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    onClick = onDismiss,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                }
                Text(
                    text = "¿Cómo se llama el grupo que querés añadir?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            // Custom Input
            OutlinedTextField(
                value = newGroupName,
                onValueChange = onGroupNameChanged,
                placeholder = { Text("Nombre del grupo") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            )

            // Defaults List
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "PREDETERMINADOS",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                defaultGroups.forEach { name ->
                    val isSelected = newGroupName.equals(name, ignoreCase = true)
                    Surface(
                        onClick = { onGroupNameChanged(name) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                    ) {
                        Column {
                            Text(
                                text = name,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                            if (!isSelected) {
                                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                            }
                        }
                    }
                }
            }

            // Action
            Button(
                onClick = onAddGroup,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .height(80.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(Icons.Rounded.Check, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text(text = "Añadir grupo", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddChoiceSheet(
    onAddDevice: () -> Unit,
    onAddGroup: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "¿Qué querés añadir?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            ChoiceItem(
                title = "Añadir un dispositivo",
                subtitle = "TVs, Soundbars y más.",
                icon = Icons.Rounded.Tv,
                onClick = onAddDevice
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            ChoiceItem(
                title = "Añadir un grupo",
                subtitle = "Ordená tus dispositivos por grupos como \"Habitación\", \"Cocina\", etc.",
                icon = Icons.Rounded.Air,
                onClick = onAddGroup
            )
        }
    }
}

@Composable
fun ChoiceItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Column {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ConnectingScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            RemotitoAppBar(title = "Conectando...", onBackClick = onBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(46.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = "Estamos estableciendo conexión a nuestro servidor",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Aguardá un momento...",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ContainedLoadingIndicator(
                    modifier = Modifier.size(48.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerShape = RoundedCornerShape(24.dp)
                )
            }
        }
    }
}
